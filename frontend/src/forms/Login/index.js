import React, {useEffect, useState} from 'react';
import {connect} from "../../App/Context";
import MenuItem from "@material-ui/core/MenuItem";
import * as PropTypes from "prop-types";
import './style.css'
import {getLoginId, postLogin, setCsrfToken, setUserId} from "./actions";
import LabeledInput from "../utils/components/LabeledInput";
import ItemContainer from "../utils/components/Container/ItemContainer";
import ContainerColumn from "../utils/components/Container/ContainerColumn";
import {navigate} from "hookrouter";
import CustomButton from "../utils/components/CustomButton";
import {set as setCookie} from "js-cookie";
import {USER_ID} from "../utils/constants";
import {CSRF_TOKEN} from "../../App/Context/constants";

const FormLogin = ({
                       formId, postLogin, handleMenuClose, loginId, getLoginId,
                       setUserId, csrfToken, setCsrfToken, loginToken
                   }) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    useEffect(() => {
        if (loginToken.token && !loginToken.inProcess && login && login !== "") {
            setCookie(CSRF_TOKEN, loginToken.token);
            setCsrfToken(loginToken.token);
            getLoginId(login);
        }
    }, [loginToken, getLoginId, login, setCsrfToken]);

    useEffect(() => {
        if (loginId.response.id) {
            setCookie(USER_ID, loginId.response.id);
            setUserId(loginId.response.id);
        }
    }, [loginId, setUserId]);

    const handleSubmit = () => {
        postLogin(login, password);
    };

    return <ContainerColumn>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <ItemContainer>
                <LabeledInput
                    id="login"
                    label="Login"
                    value={login}
                    onChange={setLogin}
                />
            </ItemContainer>
        </MenuItem>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <ItemContainer>
                <LabeledInput
                    type='password'
                    id="password"
                    label="Password"
                    value={password}
                    onChange={setPassword}
                />
            </ItemContainer>
        </MenuItem>
        <MenuItem button={true}
                  component={formId + 'login'}>
            <ItemContainer>
                <CustomButton text='Login'
                              onClick={handleSubmit}/>
            </ItemContainer>
        </MenuItem>
        <MenuItem button={false}
                  component={formId + 'login'}>
            <ItemContainer>
                <CustomButton text='Sign up'
                              onClick={() => {
                                  navigate('/register');
                                  handleMenuClose();
                              }}/>
            </ItemContainer>
        </MenuItem
        >
    </ContainerColumn>;
};

FormLogin.propTypes = {
    formId: PropTypes.string.isRequired,
    postLogin: PropTypes.func.isRequired,
    handleMenuClose: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    loginId: state.components.forms.login.loginId,
    loginToken: state.components.forms.login.loginToken,
    csrfToken: state.csrfToken
});

export default connect(mapStateToProps,
    {postLogin, getLoginId, setUserId, setCsrfToken})(FormLogin);