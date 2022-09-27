import {NextPage} from "next";
import DefaultPageContainer from "../../components/DefaultPageContainer";
import {useGetBotGuilds} from "../../components/util/SelfAPIRequest";
import {LoadingSpinner} from "../../components/loading/LoadingSpinner";
import {useEffect} from "react";
import {useSession} from "next-auth/react";

const Servers: NextPage = () => {
    const botGuildsHook = useGetBotGuilds(typeof window !== "undefined" ? window.location.hostname : "");
    const {data: session, status} = useSession();

    useEffect(() => {
        if (!botGuildsHook.isLoading && !botGuildsHook.isError) {

        }
    }, [botGuildsHook, status])

    if (botGuildsHook.isLoading) {
        return (
            <DefaultPageContainer title={"Loading..."}>
                <div className={"grid content-center"}>
                    <LoadingSpinner/>
                </div>
            </DefaultPageContainer>
        )
    }

    if (botGuildsHook.isError) {
        return (
            <DefaultPageContainer title={"Loading..."}>
                <div className={"grid content-center"}>
                    {botGuildsHook.isError.toString()}
                </div>
            </DefaultPageContainer>
        );
    }

    return (
        <DefaultPageContainer title={"Server Name"}>
            <div>
                WIP
            </div>
        </DefaultPageContainer>
    )
}

export default Servers