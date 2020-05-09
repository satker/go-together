import React, {useEffect, useState} from 'react';
import {connect} from "../../App/Context";
import MenuItem from "@material-ui/core/MenuItem";
import * as PropTypes from "prop-types";
import './style.css'
import {getLoginId, postLogin, setUserId} from "./actions";
import LabeledInput from "../utils/LabeledInput";
import ItemContainer from "../utils/components/Container/ItemContainer";
import ContainerColumn from "../utils/components/Container/ContainerColumn";
import {navigate} from "hookrouter";
import CustomButton from "../utils/components/CustomButton";
import {set as setCookie} from "js-cookie";
import {USER_ID} from "../utils/constants";

const FormLogin = ({formId, postLogin, handleMenuClose, loginId, getLoginId, setUserId, csrfToken}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [startLogin, setStartLogin] = useState(false);

    useEffect(() => {
        console.log(startLogin, csrfToken);
        if (startLogin && csrfToken) {
            getLoginId(login);
            setStartLogin(false)
        }
    }, [csrfToken, getLoginId, login, startLogin]);

    useEffect(() => {
        if (loginId.response.id) {
            setCookie(USER_ID, loginId.response.id);
            setUserId(loginId.response.id);
        }
    }, [loginId, setUserId]);

    const handleSubmit = () => {
        postLogin(login, password);
        setStartLogin(true);
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
    csrfToken: state.csrfToken
});

export default connect(mapStateToProps, {postLogin, getLoginId, setUserId})(FormLogin);