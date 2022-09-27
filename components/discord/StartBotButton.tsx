import {postRequest, useGetBotStatus} from "../util/SelfAPIRequest";
import {useSession} from "next-auth/react";
import {useEffect, useState} from "react";

export function StartBotButton() {
    const botStatus = useGetBotStatus(typeof window !== "undefined" ? window.location.hostname : "");
    const [token, setToken] = useState<string | undefined>(undefined);
    const {data: session, status} = useSession();

    useEffect(() => {
        if (status === "authenticated") {
            setToken(String(session.accessToken));
        }
    }, [status,session]);

    if (botStatus.data === undefined || token === undefined) {
        return (
            <></>
        )
    }

    if (botStatus.data.isReady) {
        return (
            <button
                type={"button"}
                className={"inline-flex items-center rounded-md border border-transparent bg-red-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"}
                onClick={async () => {
                    await postRequest("/api/bot", {
                        action: "shutdown"
                    }, token);
                }}
            >
                Shutdown
            </button>
        )
    }

    return (
        <button
            type={"button"}
            className={"inline-flex items-center rounded-md border border-transparent bg-green-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"}
            onClick={async () => {
                await postRequest("/api/bot", {
                    action: "start"
                }, token);
            }}
        >
            Start
        </button>
    )
}