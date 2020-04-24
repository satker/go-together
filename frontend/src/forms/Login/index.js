import React, {useState} from 'react';
import {connect} from "../../App/Context";
import MenuItem from "@material-ui/core/MenuItem";
import * as PropTypes from "prop-types";
import './style.css'
import {FORM_ID} from "./constants";
import {postLogin} from "./actions";
import LabeledInput from "../utils/LabeledInput";
import ItemContainer from "../utils/components/Container/ItemContainer";
import ContainerColumn from "../utils/components/Container/ContainerColumn";
import {navigate} from "hookrouter";
import CustomButton from "../utils/components/CustomButton";

const FormLogin = ({formId, postLogin, handleMenuClose}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = () => {
        postLogin(login, password);
    };

    return <ContainerColumn formId={FORM_ID}>
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

export default connect(null, {postLogin})(FormLogin)(FORM_ID);