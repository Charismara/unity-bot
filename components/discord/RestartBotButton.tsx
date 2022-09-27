import {postRequest, useGetBotStatus} from "../util/SelfAPIRequest";
import {useEffect, useState} from "react";
import {useSession} from "next-auth/react";

export function RestartBotButton() {
    const botStatus = useGetBotStatus(typeof window !== "undefined" ? window.location.hostname : "");
    const [token, setToken] = useState<string | undefined>(undefined);
    const {data: session, status} = useSession();

    useEffect(() => {
        if (status === "authenticated") {
            setToken(String(session.accessToken));
        }
    }, [status, session]);

    if (botStatus.data === undefined || !botStatus.data.isReady || token === undefined) {
        return (
            <></>
        )
    }

    return (
        <button
            type={"button"}
            className={"inline-flex items-center rounded-md border border-transparent bg-teal-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"}
            onClick={async () => {
                await postRequest("/api/bot", {
                    action: "restart"
                }, token);
            }}
        >
            Restart
        </button>
    )
}