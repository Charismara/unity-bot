import {Client, GatewayIntentBits} from "discord.js";

export class Bot {
    private static BOT: Client | undefined = undefined

    static async getBot() {
        if (Bot.BOT === undefined) {
            console.log("Starting Discord Bot...")
            Bot.BOT = new Client({
                intents: [
                    GatewayIntentBits.Guilds
                ]
            });
            await this.start();
        }

        return Bot.BOT;
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