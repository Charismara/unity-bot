import {useGetUserGuilds} from "../util/DiscordAPIRequest";
import {useGetBotGuilds} from "../util/SelfAPIRequest";
import {LoadingSpinner} from "../loading/LoadingSpinner";
import {PlusCircleIcon} from "@heroicons/react/24/outline";
import Link from "next/link";

type Props = {
    token: string
}

type GuildData = {
    id: string,
    name: string,
    icon: string,
    canInviteBot: boolean,
    canManageBot: boolean
}

export function UserGuildGrid({token}: Props) {
    const {guilds, isLoading, isError} = useGetUserGuilds(token);
    const botGuildsHook = useGetBotGuilds(typeof window !== "undefined" ? window.location.hostname : "");

    if (isLoading || botGuildsHook.isLoading) {
        return (
            <>
                <div className={"grid content-center w-full h-full"}>
                    <LoadingSpinner/>
                </div>
            </>
        )
    }

    if (isError) {
        return (
            <>
                <div>
                    {isError.toString()}
                </div>
            </>
        )
    }

    if (botGuildsHook.isError) {
        return (
            <>
                <div>
                    {botGuildsHook.isError.toString()}
                </div>
            </>
        )
    }

    const guildData: GuildData[] = []

    guilds?.filter(guild => (guild.permissions as any & 0x20) === 0x20).forEach(guild => {
        guildData.push({
            id: guild.id,
            name: guild.name,
            icon: guild.icon,
            canInviteBot: botGuildsHook.data?.guilds?.find(botData => botData.id == guild.id) === undefined,
            canManageBot: true
        });
    });


    return (
        <>
            <div className="mx-auto max-w-7xl py-12 px-4 text-center sm:px-6 lg:px-8 lg:py-24">
                <div className="space-y-12">
                    <div className="space-y-5 sm:mx-auto sm:max-w-xl sm:space-y-4 lg:max-w-5xl">
                        <h2 className="text-3xl font-bold tracking-tight sm:text-4xl">Select a Server</h2>
                        <p className="text-xl text-gray-500">
                            Only Servers with <span className={"italic"}>MANAGE_GUILD (in Discord)</span> or <span className={"italic"}>Bot Management (in the Bot Dashboard)</span> permission are visible.
                        </p>
                    </div>
                    <ul
                        role="list"
                        className="mx-auto space-y-16 sm:grid sm:grid-cols-2 sm:gap-16 sm:space-y-0 lg:max-w-5xl lg:grid-cols-3"
                    >
                        {guildData!.map((guild) => (
                            <li key={String(guild.id)}>
                                <Link href={guild.canInviteBot ? "https://discord.com/api/oauth2/authorize?client_id=907572774439112754&permissions=8&scope=bot%20applications.commands&guild_id=" + guild.id : "/servers/" + guild.id}
                                      target={guild.canInviteBot ? "_blank" : undefined}>
                                    <a target={guild.canInviteBot ? "_blank" : undefined}>
                                        <div className="space-y-6 hover:bg-gray-300 px-2 py-2 rounded-xl">
                                            <div>
                                                <img className="mx-auto h-40 w-40 rounded-full xl:h-56 xl:w-56" src={"https://cdn.discordapp.com/icons/" + guild.id + "/" + guild.icon + ".png"} alt="" onError={event => {
                                                    event.preventDefault();
                                                    event.currentTarget.style.display = "none"
                                                    const placeholder = event.currentTarget.ownerDocument.getElementById(guild.id);
                                                    if (placeholder) {
                                                        placeholder.style.display = "block"
                                                    }
                                                }}/>
                                                <div className={"mx-auto h-40 w-40 rounded-full xl:h-56 xl:w-56 mt-0 bg-gray-500"} style={{display: "none"}} id={guild.id}>
                                                    <div className={"grid content-center h-full"}>
                                                        <span className={"text-3xl font-bold tracking-tight sm:text-4xl text-white"}>?</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="space-y-2">
                                                <div className="space-y-1 text-lg font-medium leading-6">
                                                    <h3>{guild.name}</h3>
                                                </div>
                                                <ul role="list" className="flex justify-center space-x-5">
                                                    {guild.canInviteBot ?
                                                        <li>
                                                            <span className={"text-gray-700"}>Invite Bot</span>
                                                        </li>
                                                        :
                                                        <li>
                                                            <span className={"text-gray-700"}>Manage Bot</span>
                                                        </li>
                                                    }
                                                </ul>
                                            </div>
                                        </div>
                                    </a>
                                </Link>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </>
    )
}