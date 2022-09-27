import {Client, CommandInteraction, GatewayIntentBits, Interaction, SlashCommandBuilder} from "discord.js";
import {Commands} from "./Commands";

export class Bot {
    private static BOT: Client | undefined = undefined

    static async getBot() {
        if (Bot.BOT === undefined) {
            console.log("Starting Discord Bot...")
            Bot.BOT = new Client({
                intents: [
                    GatewayIntentBits.Guilds
                ]
            })
                .on('ready', async () => {
                    if (!Bot.BOT!.user || !Bot.BOT!.application) return;
                    await Bot.BOT!.application.commands.set(Commands.map(commands => {
                        const builder = new SlashCommandBuilder();
                        builder.setName(commands.name)
                            .setDescription(commands.description);

                        commands.applyOptions(builder);
                        return builder.toJSON();
                    }));
                })
                .on("interactionCreate", async (interaction: Interaction) => {
                    if (interaction.isCommand()) {
                        await this.handleCommand(this.BOT!, interaction);
                    }
                })
            ;
            await this.start();
        }

        return Bot.BOT;
    }

    static handleCommand = async (client: Client, interaction: CommandInteraction) => {
        const command = Commands.find(command => command.name === interaction.commandName);
        if (!command) {
            await interaction.followUp({content: 'An error has occurred'});
            return
        }

        await interaction.deferReply();
        command.run(client, interaction);
    }


    static async start() {
        if (Bot.BOT === undefined) return;
        Bot.BOT.once("ready", args => {
            console.log("Bot started successfully!")
        })

        await Bot.BOT.login(process.env.DISCORD_BOT_TOKEN)
    }

    static async stop() {
        if (Bot.BOT === undefined) return;
        console.log("Shutting down Discord Bot...");
        Bot.BOT.destroy();
    }
}