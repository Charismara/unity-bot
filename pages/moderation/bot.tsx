import {NextPage} from "next";
import DefaultPageContainer from "../../components/DefaultPageContainer";
import {useSession} from "next-auth/react";
import {useEffect} from "react";
import {useRouter} from "next/router";
import {PaperClipIcon} from "@heroicons/react/24/outline";
import {BotStatus} from "../../components/discord/BotStatus";
import {StartBotButton} from "../../components/discord/StartBotButton";
import {RestartBotButton} from "../../components/discord/RestartBotButton";
import {UnityUser} from "../api/auth/[...nextauth]";

const Bot: NextPage = () => {
    const {data: session, status} = useSession();
    const router = useRouter();

    useEffect(() => {
        if (status == "loading") return;
        if (status == "unauthenticated") {
            router.push('/')
        }
        if (status === "authenticated") {
            if ((session?.user as UnityUser).role !== "ADMIN") {
                router.push('/')
            }
        }
    }, [router, status, session])

    return (
        <DefaultPageContainer title={"Bot Moderation"}>
            <div className={"w-full py-5"}>
                <div className="overflow-hidden bg-white shadow sm:rounded-lg w-5/6 mx-auto">
                    <div className="px-4 py-5 sm:px-6">
                        <h3 className="text-lg font-medium leading-6 text-gray-900">Bot Information</h3>
                        <p className="mt-1 max-w-2xl text-sm text-gray-500">Bot details and useful data.</p>
                    </div>
                    <div className="border-t border-gray-200 px-4 py-5 sm:p-0">
                        <dl className="sm:divide-y sm:divide-gray-200">
                            <BotStatus/>
                            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                                <dt className="text-sm font-medium text-gray-500">Application for</dt>
                                <dd className="mt-1 text-sm text-gray-900 sm:col-span-2 sm:mt-0">Backend Developer</dd>
                            </div>
                            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                                <dt className="text-sm font-medium text-gray-500">Email address</dt>
                                <dd className="mt-1 text-sm text-gray-900 sm:col-span-2 sm:mt-0">margotfoster@example.com</dd>
                            </div>
                            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                                <dt className="text-sm font-medium text-gray-500">Salary expectation</dt>
                                <dd className="mt-1 text-sm text-gray-900 sm:col-span-2 sm:mt-0">$120,000</dd>
                            </div>
                            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                                <dt className="text-sm font-medium text-gray-500">About</dt>
                                <dd className="mt-1 text-sm text-gray-900 sm:col-span-2 sm:mt-0">
                                    Fugiat ipsum ipsum deserunt culpa aute sint do nostrud anim incididunt cillum culpa consequat. Excepteur
                                    qui ipsum aliquip consequat sint. Sit id mollit nulla mollit nostrud in ea officia proident. Irure nostrud
                                    pariatur mollit ad adipisicing reprehenderit deserunt qui eu.
                                </dd>
                            </div>
                            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:py-5 sm:px-6">
                                <dt className="text-sm font-medium text-gray-500">Attachments</dt>
                                <dd className="mt-1 text-sm text-gray-900 sm:col-span-2 sm:mt-0">
                                    <ul role="list" className="divide-y divide-gray-200 rounded-md border border-gray-200">
                                        <li className="flex items-center justify-between py-3 pl-3 pr-4 text-sm">
                                            <div className="flex w-0 flex-1 items-center">
                                                <div className={"mx-1"}>
                                                    <StartBotButton/>
                                                </div>
                                                <div className={"mx-1"}>
                                                    <RestartBotButton/>
                                                </div>
                                            </div>
                                        </li>
                                        <li className="flex items-center justify-between py-3 pl-3 pr-4 text-sm">
                                            <div className="flex w-0 flex-1 items-center">
                                                <PaperClipIcon className="h-5 w-5 flex-shrink-0 text-gray-400" aria-hidden="true"/>
                                                <span className="ml-2 w-0 flex-1 truncate">coverletter_back_end_developer.pdf</span>
                                            </div>
                                            <div className="ml-4 flex-shrink-0">
                                                <a href="#" className="font-medium text-indigo-600 hover:text-indigo-500">
                                                    Download
                                                </a>
                                            </div>
                                        </li>
                                    </ul>
                                </dd>
                            </div>
                        </dl>
                    </div>
                </div>
            </div>
        </DefaultPageContainer>
    )
}

export default Bot