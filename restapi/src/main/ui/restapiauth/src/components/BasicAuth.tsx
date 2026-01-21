import React, {useEffect, useState} from "react";
import {AuthPayload, AuthType} from "./AuthPage";
import {getCredentials} from "../api";

const BasicAuth = ({onAuthChange}: { onAuthChange: (authPayload: AuthPayload) => void }) => {
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [apiUrl, setApiUrl] = useState("");
    const [savedCred, setSavedCred] = useState<boolean>(false);

    useEffect(() => {
        getAllCred().catch(error => console.log(error));
    }, []);

    useEffect(() => {
        onAuthChange({
            authType: AuthType.Basic,
            apiUrl: apiUrl,
            userName: userName,
            password: password,
            isAllRequiredFieldsHaveValue: areFieldsValid(),
            isCredentialsSaved: isCredSaved()
        });
    }, [userName, password, apiUrl]);

    const areFieldsValid = () => {
        return !!userName && !!password && !!apiUrl;
    };

    const isCredSaved = (): boolean =>{
        return savedCred;
    }

    const getAllCred = async () => {
        try {
            const credentials = await getCredentials(AuthType.Basic);
            if (credentials) {
                setUserName(credentials["Username"]);
                setPassword(credentials["password"]);
                setApiUrl(credentials["Api Url"]);
                setSavedCred(true);
            } else {
                console.error('Failed to fetch credentials');
            }
        } catch (error) {
            console.error('Error fetching credentials:', error);
        }
    }

    return (
        <div>
            <div
                className="auth-form">
                <div className="form-group">
                    <label htmlFor="username">Username<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="text"
                        id="username"
                        value={userName}
                        onChange={(e) => setUserName(e.target.value)}
                        placeholder="Enter Username"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter Password"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="auth-url">API URL<span className="mandatory-asterisk">*</span></label>
                    <input
                        type="text"
                        value={apiUrl}
                        onChange={(e) => setApiUrl(e.target.value)}
                        id="apiUrl"
                        placeholder="Enter API URL"
                    />
                </div>
            </div>
        </div>
    );
};

export default BasicAuth;
