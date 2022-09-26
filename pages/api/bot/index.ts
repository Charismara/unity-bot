import {NextApiRequest, NextApiResponse} from "next";
import {Bot} from "../../../components/bot/Bot";
import {getSession} from "next-auth/react";

export type BotStatus = {
    isReady?: boolean,
    message?: string
}

export default async function handle(req: NextApiRequest, res: NextApiResponse<BotStatus>) {
    const session = await getSession({req})
    const bot = await Bot.getBot();
    //Get Bot Status
    if (req.method === 'GET') {
        if (session) {
            if (session.user?.role === "MODERATOR" || session.user?.role === "ADMIN") {
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
        //TODO
    }
}