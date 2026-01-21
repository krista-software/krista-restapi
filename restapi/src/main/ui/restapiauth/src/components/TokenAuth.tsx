import React, {useEffect, useState} from "react";
import {AuthPayload, AuthType} from "./AuthPage";
import {getCredentials} from "../api";

// Added onAuthChange prop of type function that receives an AuthPayload as parameter.
const TokenAuth = ({onAuthChange}: { onAuthChange: (authPayload: AuthPayload) => void }) => {
    const [token, setToken] = useState("");
    const [tokenType, setTokenType] = useState("");
    const [apiUrl, setApiUrl] = useState("");
    const [savedCred, setSavedCred] = useState<boolean>(false);

    useEffect(() => {
        getAllCred().catch(error => console.log(error));
    }, []);

    useEffect(() => {
        onAuthChange({
            authType: AuthType.Token,
            apiUrl: apiUrl,
            tokenType: tokenType,
            token: token,
            isAllRequiredFieldsHaveValue: areFieldsValid(),
            isCredentialsSaved: isCredSaved()
        });
    }, [apiUrl, tokenType, token, onAuthChange]);

    const areFieldsValid = () => {
        return !!apiUrl && !!token;
    };

    const isCredSaved = (): boolean =>{
                return savedCred;
    }
    const getAllCred = async () => {
        try {
            const credentials = await getCredentials(AuthType.Token);
            if (credentials) {
                setToken(credentials["token"]);
                setTokenType(credentials["Token Type"]);
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
            <div className="auth-form">
                <div className="form-group">
                    <label htmlFor="token">
                        Token
                        <span className="mandatory-asterisk">*</span>
                    </label>
                    <input
                        type="text"
                        value={token}
                        onChange={(e) => setToken(e.target.value)}
                        id="token"
                        placeholder="Enter Token"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="token-type">
                        Token Type
                        <span className="mandatory-asterisk">*</span>
                    </label>
                    <input
                        type="text"
                        value={tokenType}
                        onChange={(e) => setTokenType(e.target.value)}
                        id="tokenType"
                        placeholder="Enter Token Type"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="api-url">
                        API URL
                        <span className="mandatory-asterisk">*</span>
                    </label>
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

export default TokenAuth;
