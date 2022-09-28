import {signIn, signOut, useSession} from "next-auth/react";
import {Menu, Transition} from '@headlessui/react'
import {
    ArrowRightOnRectangleIcon,
    ChevronDownIcon,
    PencilSquareIcon,
    ShieldCheckIcon,
    ShieldExclamationIcon
} from "@heroicons/react/24/outline";
import {Fragment} from "react";
import Link from "next/link";
import {UnityUser} from "../../pages/api/auth/[...nextauth]";

type Props = {
    className?: string
}


function classNames(...classes: string[]) {
    return classes.filter(Boolean).join(' ')
}

export function LoginButton(props: Props) {
    const {data: session, status} = useSession();
    if (session && status === "authenticated") {
        const user = session.user as UnityUser
        const managementControls = [];
        if (user.role == "MODERATOR" || user.role == "ADMIN") {
            managementControls.push({
                title: "Bot Moderation",
                icon: ShieldCheckIcon,
                href: "/moderation/bot"
            })
        }

        if (user.role == "ADMIN") {
            managementControls.push({
                title: "User Moderation",
                icon: ShieldExclamationIcon,
                href: "/moderation/users"
            })
        }

        return (
            <>
                <Menu as="div" className="text-left hidden md:relative md:inline-block">
                    <div>
                        <Menu.Button
                            className="inline-flex w-full justify-center bg-white px-4 py-2 text-sm font-medium text-gray-700 focus:outline-none">
                            <img src={user.image === null ? undefined : user.image} alt={""} className={"h-5 w-5 mr-1"}/>
                            {user.name}
                            <ChevronDownIcon className="-mr-1 ml-2 h-5 w-5" aria-hidden="true"/>
                        </Menu.Button>
                    </div>

                    <Transition
                        as={Fragment}
                        enter="transition ease-out duration-100"
                        enterFrom="transform opacity-0 scale-95"
                        enterTo="transform opacity-100 scale-100"
                        leave="transition ease-in duration-75"
                        leaveFrom="transform opacity-100 scale-100"
                        leaveTo="transform opacity-0 scale-95"
                    >
                        <Menu.Items className="absolute right-0 z-10 mt-2 w-56 origin-top-right divide-y divide-gray-100 rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                            {
                                managementControls.length > 0 ? <div className="py-1">
                                    {managementControls.map(item => {
                                        return (
                                            <Menu.Item key={item.title}>
                                                {({active}) => (
                                                    <div className={classNames(
                                                        active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                                                        'group flex items-center px-4 py-2 text-sm'
                                                    )}>
                                                        <Link href={item.href}>
                                                            <a
                                                                className={'group flex items-center text-sm'}
                                                            >
                                                                <item.icon className="mr-3 h-5 w-5 text-gray-400 group-hover:text-gray-500"
                                                                           aria-hidden="true"/>
                                                                {item.title}
                                                            </a>
                                                        </Link>
                                                    </div>
                                                )}
                                            </Menu.Item>
                                        )
                                    })}
                                </div> : undefined
                            }
                            <div className="py-1">
                                <Menu.Item>
                                    {({active}) => (
                                        <div className={classNames(
                                            active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                                            'group flex items-center px-4 py-2 text-sm'
                                        )}>
                                            <Link href={"/servers"}>
                                                <a className={'group flex items-center text-sm'}>
                                                    <PencilSquareIcon className="mr-3 h-5 w-5 text-gray-400 group-hover:text-gray-500" aria-hidden="true"/>
                                                    Server Management
                                                </a>
                                            </Link>
                                        </div>
                                    )}
                                </Menu.Item>
                            </div>
                            <div className="py-1">
                                <Menu.Item>
                                    {({active}) => (
                                        <div
                                            className={classNames(
                                                active ? 'bg-gray-100 text-gray-900' : 'text-gray-700',
                                                'group flex items-center px-4 py-2 text-sm'
                                            )}
                                            onClick={() => signOut()}
                                            style={{cursor: "pointer"}}
                                        >
                                            <ArrowRightOnRectangleIcon className="mr-3 h-5 w-5 text-gray-400 group-hover:text-gray-500" aria-hidden="true"/>
                                            Sign out
                                        </div>
                                    )}
                                </Menu.Item>
                            </div>
                        </Menu.Items>
                    </Transition>
                </Menu>
                <div className="mt-6 md:hidden">
                    <div className="grid gap-y-8">
                        {
                            managementControls.length > 0 ?
                                managementControls.map(item => {
                                    return (
                                        <div key={item.title} className="-m-3 flex items-center rounded-md p-3 hover:bg-gray-50">
                                            <Link href={item.href}>
                                                <a className="-m-3 flex items-center rounded-md p-3 hover:bg-gray-50">
                                                    <item.icon className="h-6 w-6 flex-shrink-0 text-indigo-600" aria-hidden="true"/>
                                                    <span className="ml-3 text-base font-medium text-gray-900">{item.title}</span>
                                                </a>
                                            </Link>
                                        </div>
                                    )
                                }) : undefined
                        }
                        <div className="-m-3 flex items-center rounded-md p-3 hover:bg-gray-50" onClick={() => signOut()} style={{cursor: "pointer"}}>
                            <ArrowRightOnRectangleIcon className="h-6 w-6 flex-shrink-0 text-indigo-600" aria-hidden="true"/>
                            <span className="ml-3 text-base font-medium text-gray-900">Sign out</span>
                        </div>
                    </div>
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