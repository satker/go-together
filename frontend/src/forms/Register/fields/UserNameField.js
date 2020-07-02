import React, {useEffect, useState} from 'react';

import LabeledInput from "forms/utils/components/LabeledInput";
import {connect} from "App/Context";

import {EMPTY_LOGIN, GOOD_LOGIN, NOT_GOOD_LOGIN} from "../constants";
import {handleUserName} from "../validation";
import {getCheckUserName} from "../actions";

const UserNameField = ({login, setLogin, checkUserName, getCheckUserName, setIsIncorrectData}) => {
    const [checkedUserName, setCheckedUserName] = useState(EMPTY_LOGIN);
    const [isUserNameReadyForRegister, setIsUserNameReadyForRegister] = useState(false);

    useEffect(() => {
        if (checkUserName.response === true) {
            setCheckedUserName(NOT_GOOD_LOGIN);
            setIsUserNameReadyForRegister(false);
        } else {
            setCheckedUserName(GOOD_LOGIN);
            setIsUserNameReadyForRegister(true);
        }
    }, [checkUserName, setCheckedUserName, setIsUserNameReadyForRegister]);

    const checkResult = (isCorrect) => {
        setIsIncorrectData(isCorrect);
        setIsUserNameReadyForRegister(isCorrect);
    };

    return <LabeledInput
        isError={!isUserNameReadyForRegister}
        id="login"
        label="Login"
        errorText={checkedUserName}
        value={login}
        onChange={(value) => {
            setLogin(value);
            handleUserName(value, setCheckedUserName, checkResult, getCheckUserName);
        }
        }
    />;
};

const mapStateToProps = state => ({
    checkUserName: state.components.forms.register.checkUserName,
});

export default connect(mapStateToProps, {getCheckUserName})(UserNameField);