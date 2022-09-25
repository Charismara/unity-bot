import {Head, Html, Main, NextScript} from "next/document";

export default function Document() {
    return (
        <Html>
            <Head>
                <meta name="description" content="An open source universal Discord Bot with Dashboard access"/>
                <link rel="icon" href="/favicon.ico"/>
                <link rel="stylesheet" href="https://rsms.me/inter/inter.css"/>
            </Head>
            <body>
            <Main/>
            <NextScript/>
            </body>
        </Html>
    )
}