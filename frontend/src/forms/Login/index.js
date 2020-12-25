import React, {useState} from 'react';
import PropTypes from "prop-types";
import MenuItem from "@material-ui/core/MenuItem";
import {navigate} from "hookrouter";

import {connect} from "App/Context";
import LabeledInput from "forms/utils/components/LabeledInput";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import CustomButton from "forms/utils/components/CustomButton";

import {postLogin} from "./actions";
import './style.css'

const Login = ({formId, postLogin, handleMenuClose}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

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

Login.propTypes = {
    formId: PropTypes.string.isRequired,
    postLogin: PropTypes.func.isRequired,
    handleMenuClose: PropTypes.func.isRequired
};

export default connect(null, {postLogin})(Login);