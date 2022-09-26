import {GetServerSideProps, InferGetServerSidePropsType, NextPage} from "next";
import DefaultPageContainer from "../components/DefaultPageContainer";
import {useSession} from "next-auth/react";
import {LoadingSpinner} from "../components/loading/LoadingSpinner";
import {useRouter} from "next/router";
import {useEffect} from "react";
import {useGetUserGuilds} from "../components/util/DiscordAPIRequest";
import {Bot} from "../components/bot/Bot";

const Servers: NextPage = ({data, test}: InferGetServerSidePropsType<typeof getServerSideProps>) => {
    const {data: session, status} = useSession();
    const router = useRouter();
    const {guilds, isLoading, isError} = useGetUserGuilds(String(session!.accessToken));

    useEffect(() => {
        if (!session) {
            router.push('/');
        }
    }, [router, session])

    if (status === "loading") {
        return (
            <DefaultPageContainer title={"Servers"}>
                <div className={"grid w-full justify-items-center mt-2"}>
                    <LoadingSpinner/>
                </div>
            </DefaultPageContainer>
        )
    }

    console.log(test, data);

    return (
        <DefaultPageContainer title={"Servers"}>
            <div>
                {isLoading ? <span>Loading Guilds...</span> : isError ? <span>{isError}</span> : <span>Loaded {guilds!.length} Guilds</span>}
            </div>
        </DefaultPageContainer>
    )
}

export const getServerSideProps: GetServerSideProps = async (context) => {
    const bot = await Bot.getBot();
    const data = bot.guilds.cache;
    const test = "asdf"
    console.log("Server side", data);
    return {
        props: {data, test}, // will be passed to the page component as props
    }
}


export default Servers