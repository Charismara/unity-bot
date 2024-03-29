import {NextApiRequest, NextApiResponse} from "next";
import {getSession} from "next-auth/react";
import {UnityUser} from "../auth/[...nextauth]";
import {Client} from "discord.js";

export type BotStatus = {
    isReady?: boolean,
    message?: string
}

export type BotStatusChangeRequest = {
    "action": "shutdown" | "start" | "restart"
}

export default async function handle(req: NextApiRequest, res: NextApiResponse<BotStatus>) {
    const session = await getSession({req})
    const bot = (req as any).discordBot as Client;

    //Get Bot Status
    if (req.method === 'GET') {
        if (session) {
            if ((session.user as UnityUser).role === "MODERATOR" || (session.user as UnityUser).role === "ADMIN") {
                res.status(200).json({isReady: bot.isReady()})
            } else {
                res.status(403).json({
                    message: "You don't have permission to access this API"
                })
            }
        } else {
            res.status(403).json({
                message: 'You must be sign in to access this API'
            })
        }
        return;
    }

    //Set Bot Status
    if (req.method === 'POST') {
        if (session) {
            if ((session.user as UnityUser).role === "ADMIN") {
                const body = JSON.parse(req.body) as BotStatusChangeRequest;
                if (body) {
                    let success = false;
                    if (body.action === "start") {
                        console.log("Starting Discord bot. Requested by user with id: ", JSON.stringify((session.user as UnityUser).id));
                        await bot.login(process.env.DISCORD_BOT_TOKEN);
                        success = true;
                    }

                    if (body.action === "shutdown") {
                        console.log("Shutting down Discord bot. Requested by user with id: ", JSON.stringify((session.user as UnityUser).id));
                        bot.destroy();
                        success = true;
                    }

                    if (body.action === "restart") {
                        console.log("Restarting Discord bot. Requested by user with id: ", JSON.stringify((session.user as UnityUser).id));
                        await bot.destroy();
                        await bot.login(process.env.DISCORD_BOT_TOKEN);
                        success = true;
                    }

                    res.status(200).json({
                        message: 'Success: ' + success
                    })
                } else {
                    res.status(400).json({
                        message: 'Missing "action" in post body'
                    })
                }
            } else {
                res.status(403).json({
                    message: "You don't have permission to access this API"
                })
            }
        } else {
            res.status(403).json({
                message: 'You must be sign in to access this API'
            })
        }
        return;
    }
}