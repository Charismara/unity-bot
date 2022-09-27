import {useGetBotStatus} from "../util/SelfAPIRequest";
import {classNames} from "../util/Utils";

export function BotStatus() {
    const botStatus = useGetBotStatus(typeof window !== "undefined" ? window.location.hostname : "");

    return (
        <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Bot Status</dt>
            <dd className={classNames("mt-1 text-sm sm:col-span-2 sm:mt-0", botStatus.data === undefined ? "text-gray-900" : botStatus.data.isReady ? "text-green-600" : "text-red-500")}>{botStatus.data === undefined ? "Loading Data" : botStatus.data.isReady ? "Online" : "Offline"}</dd>
        </div>
    )
}