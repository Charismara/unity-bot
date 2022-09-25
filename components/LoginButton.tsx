import {signIn, signOut, useSession} from "next-auth/react";

type Props = {
    className?: string
}

export function LoginButton(props: Props) {
    const {data: session} = useSession();
    if (session) {
        return (
            <>
                <div className={props.className} onClick={() => signOut()} style={{cursor: "pointer"}}>
                    Sign out
                </div>
            </>
        )
    }

    return (
        <>
            <div className={props.className} onClick={() => signIn("discord")} style={{cursor: "pointer"}}>
                Sign in with Discord
            </div>
        </>
    )
}