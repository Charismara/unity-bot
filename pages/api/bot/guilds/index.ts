import {NextApiRequest, NextApiResponse} from "next";
import {getSession} from "next-auth/react";
import {Bot} from "../../../../components/bot/Bot";
import {Guild} from "discord.js";

export type BotGuilds = {
    message?: string,
    guilds?: Guild[]
}

export default async function handle(req: NextApiRequest, res: NextApiResponse<BotGuilds>) {
    const session = await getSession({req})
    const bot = await Bot.getBot();


    if (req.method === 'GET') {
        if (session) {
            if (bot === undefined) {
                res.status(503).json({message: "Discord Bot could not be initialized. Please try again."})
            } else {
                if (session.user?.role === "ADMIN") {
                    res.status(200).json({guilds: bot.guilds.cache.toJSON()})
                } else {
                    res.status(200).json({
                        guilds: bot.guilds.cache.filter(value => false).toJSON() //TODO access control
                    })
                }
            }
        } else {
            res.status(401).json({
                message: 'You must be sign in to access this API'
            })
        }
        return;
    }

    //Set Bot Status
    if (req.method === 'POST') {
        //TODO
    }
}