import {NextPage} from "next";
import DefaultPageContainer from "../components/DefaultPageContainer";
import {useSession} from "next-auth/react";
import {LoadingSpinner} from "../components/loading/LoadingSpinner";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import {UserGuildGrid} from "../components/discord/UserGuildGrid";

const Servers: NextPage = () => {
    const {data: session, status} = useSession();
    const router = useRouter();
    const [token, setToken] = useState<string | undefined>(undefined);

    useEffect(() => {
        if (!session && status !== "loading") {
            router.push('/');
        }

        if (status === "authenticated") {
            setToken(String(session!.accessToken));
        }
    }, [router, session, status])

    if (status === "loading") {
        return (
            <DefaultPageContainer title={"Servers"}>
                <div className={"grid w-full justify-items-center mt-2"}>
                    <LoadingSpinner/>
                </div>
            </DefaultPageContainer>
        )
    }

    return (
        <DefaultPageContainer title={"Servers"}>
            <div>
                {token ? <UserGuildGrid token={token}/> : <></>}
            </div>
        </DefaultPageContainer>
    )
}


export default Servers