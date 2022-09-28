import express, {Request, Response} from "express";
import next from "next";
import {Client, CommandInteraction, GatewayIntentBits, Interaction, SlashCommandBuilder} from "discord.js";
import {Commands} from "../src/bot/Commands";


const dev = process.env.NODE_ENV !== "production";
const app = next({dev});
const handle = app.getRequestHandler();
const port = process.env.PORT || 3000;
const handleCommand = async (client: Client, interaction: CommandInteraction) => {
    const command = Commands.find(command => command.name === interaction.commandName);
    if (!command) {
        await interaction.followUp({content: 'An error has occurred'});
        return
    }

    await interaction.deferReply();
    command.run(client, interaction);
};

export const discordBot = new Client({
    intents: [
        GatewayIntentBits.Guilds
    ]
});
discordBot.on('ready', async () => {
    if (discordBot.user || discordBot.application) return;
    await discordBot.application!.commands.set(Commands.map(commands => {
        const builder = new SlashCommandBuilder();
        builder.setName(commands.name)
            .setDescription(commands.description);

        commands.applyOptions(builder);
        return builder.toJSON();
    }));
})
    .on("interactionCreate", async (interaction: Interaction) => {
        if (interaction.isCommand()) {
            await handleCommand(discordBot, interaction);
        }
    });

interface BotRequest extends Request {
    discordBot: Client
}

(async () => {
    try {
        await app.prepare();
        const server = express();
        server.all("*", (req: Request, res: Response) => {
            if (req.path.startsWith("/api/bot")) {
                (req as BotRequest).discordBot = discordBot;
            }
            return handle(req, res);
        });
        server.listen(port, (err?: any) => {
            if (err) throw err;
            console.log(`> Ready on localhost:${port} - env ${process.env.NODE_ENV}`);
        });
    } catch (e) {
        console.error(e);
        process.exit(1);
    }
})();