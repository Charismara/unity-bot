import {NextApiRequest, NextApiResponse} from "next";
import {getSession} from "next-auth/react";
import {Client, Guild} from "discord.js";
import {UnityUser} from "../../auth/[...nextauth]";

export type BotGuilds = {
    message?: string,
    guilds?: Guild[]
}

export default async function handle(req: NextApiRequest, res: NextApiResponse<BotGuilds>) {
    const session = await getSession({req})
    const bot = (req as any).discordBot as Client;

    if (req.method === 'GET') {
        if (session) {
            if (bot === undefined) {
                res.status(503).json({message: "Discord Bot could not be initialized. Please try again."})
            } else {
                if ((session.user as UnityUser).role === "ADMIN") {
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