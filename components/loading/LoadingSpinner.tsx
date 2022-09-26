import styles from "../login/LoginButton.module.css";

export function LoadingSpinner() {
    return (
        <>
            <div className={styles.spinner}>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
            </div>
            <div><span className={"text-indigo-600"}>Loading</span></div>
        </>
    )
}