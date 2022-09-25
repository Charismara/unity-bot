import DiscordProvider from "next-auth/providers/discord"
import NextAuth, {NextAuthOptions} from "next-auth";
import {PrismaClient} from "@prisma/client";
import {PrismaAdapter} from "@next-auth/prisma-adapter";

const prima = new PrismaClient();

export const authOptions: NextAuthOptions = {
    adapter: PrismaAdapter(prima),
    providers: [
        DiscordProvider({
            clientId: process.env.DISCORD_CLIENT_ID!,
            clientSecret: process.env.DISCORD_CLIENT_SECRET!
        })
    ],
    callbacks: {
        async jwt({token, account}) {
            if (account) {
                token.accessToken = account?.accessToken;
            }
            return token;
        },
        async session({session, token, user}) {
            session.accessToken = token.accessToken
            return session
        }
    },
    secret: process.env.NEXTAUTH_SECRET,
    session: {
        strategy: "jwt"
    }
}

export default NextAuth(authOptions);