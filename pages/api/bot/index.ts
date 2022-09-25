import {NextApiRequest, NextApiResponse} from "next";
import {Bot} from "../../../components/bot/Bot";

export default async function handle(req: NextApiRequest, res: NextApiResponse) {
    const bot = await Bot.getBot();
    //Get Bot Status
    if (req.method === 'GET') {
        res.status(200).json({isReady: bot.isReady()})
        return;
    }

    //Set Bot Status
    if (req.method === 'POST') {
        req.body.status
    }
}