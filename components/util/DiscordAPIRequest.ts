import useSWR from "swr"

const discordAPIRequest = (token: string, url: string) => {
    return fetch('https://discord.com/api/v10/' + url, {
        method: 'GET',
        headers: {
            "Authorization": "Bearer " + token
        }
    })
}

type Guild = {
    features: string[],
    icon: string,
    id: string,
    name: string,
    owner: boolean,
    permissions: string
}

export function useGetUserGuilds(token: string) {
    const {data, error} = useSWR<Guild[]>('users/@me/guilds', args => discordAPIRequest(token, args).then(value => value.json()))
    return {
        isLoading: !error && !data,
        isError: error,
        guilds: data
    }
}