import useSWR from "swr";
import {BotStatus} from "../../pages/api/bot";
import {BotGuilds} from "../../pages/api/bot/guilds";

const getRequest = (url: string) => {
    return fetch(url, {
        method: 'GET'
    })
}

export function useGetBotStatus(obj: {}) {
    if (obj) {
    }
    const {data, error} = useSWR<BotStatus>("", () => getRequest('/api/bot').then(value => value.json()))
    return {
        isLoading: !error && !data,
        isError: error,
        data: data
    }
}

export function useGetBotGuilds(hostname: string) {
    const {data, error} = useSWR<BotGuilds>(hostname, () => getRequest('/api/bot/guilds').then(value => value.json()))
    return {
        isLoading: !error && !data,
        isError: error,
        data: data
    }
}