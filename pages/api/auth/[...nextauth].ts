import DiscordProvider from "next-auth/providers/discord"
import NextAuth, {NextAuthOptions, User} from "next-auth";
import {PrismaClient} from "@prisma/client";
import {PrismaAdapter} from "@next-auth/prisma-adapter";
import {JWT} from "next-auth/jwt";

const prima = new PrismaClient();

export type UnityUser = {
    id?: string | null;
    name?: string | null;
    email?: string | null;
    image?: string | null;
    role: "ADMIN" | "MODERATOR" | "USER" | "BANNED"
}

export const authOptions: NextAuthOptions = {
    adapter: PrismaAdapter(prima),
    providers: [
        DiscordProvider({
            clientId: process.env.DISCORD_CLIENT_ID!,
            clientSecret: process.env.DISCORD_CLIENT_SECRET!
        })
    ],
    callbacks: {
        async jwt({token, user, account}) {
            //Initial sign in
            if (account && user) {
                console.log("Logging in...")
                return {
                    accessToken: account.access_token,
                    accessTokenExpires: Date.now() + account.expires_at! * 1000,
                    refreshToken: account.refresh_token,
                    user,
                }
            }
            // Return previous token if the access token has not expired yet
            if (Date.now() < token.accessTokenExpires!) {
                console.log("Using existing token")
                return token
            }

            // Access token has expired, try to update it
            return await refreshAccessToken(token)
        },
        async session({session, token, user}) {
            session.user = token.user as User
            session.accessToken = token.accessToken
            session.error = token.error
            return session
        }
    },
    secret: process.env.NEXTAUTH_SECRET,
    session: {
        strategy: "jwt"
    }
}

export default NextAuth(authOptions);

async function refreshAccessToken(token: JWT) {
    console.log("Trying to refresh token");
    try {
        const url = "https://discord.com/api/oauth2/token?" + new URLSearchParams({
            client_id: process.env.DISCORD_CLIENT_ID!,
            clientSecret: process.env.DISCORD_CLIENT_SECRET!,
            grant_type: 'refresh_token',
            refresh_token: token.refreshToken as string
        });

        const response = await fetch(url, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            method: "POST",
        })

        const refreshedTokens = await response.json()

        if (!response.ok) {
            throw refreshedTokens
        }

        return {
            ...token,
            accessToken: refreshedTokens.access_token,
            accessTokenExpires: Date.now() + refreshedTokens.expires_at * 1000,
            refreshToken: refreshedTokens.refresh_token ?? token.refreshToken, // Fall back to old refresh token
        }
    } catch (error) {
        console.log(error)
        return {
            ...token,
            error: "RefreshAccessTokenError",
        }
    }
}