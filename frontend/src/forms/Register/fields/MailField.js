import React, {useEffect, useState} from 'react';

import LabeledInput from "forms/utils/components/LabeledInput";
import {connect} from "App/Context";

import {handleMail} from "../validation";
import {getCheckMail} from "../actions";
import {EMPTY_MAIL, GOOD_MAIL, NOT_GOOD_MAIL} from "../constants";

const MailField = ({mail, setMail, checkMail, getCheckMail, setIsIncorrectData}) => {
    const [checkedMail, setCheckedMail] = useState(EMPTY_MAIL);
    const [isMailReadyForRegister, setIsMailReadyForRegister] = useState(false);

    const checkResult = (isCorrect) => {
        setIsIncorrectData(isCorrect);
        getCheckMail(isCorrect);
    };

    useEffect(() => {
        if (checkMail.response === true) {
            setCheckedMail(NOT_GOOD_MAIL);
            setIsMailReadyForRegister(false);
        } else {
            setCheckedMail(GOOD_MAIL);
            setIsMailReadyForRegister(true);
        }
    }, [checkMail, setCheckedMail, setIsMailReadyForRegister]);

    return <LabeledInput
        isError={!isMailReadyForRegister}
        id="Email"
        label="Email"
        errorText={checkedMail}
        value={mail}
        onChange={(value) => {
            setMail(value);
            handleMail(value, setCheckedMail, checkResult, getCheckMail);
        }
        }
    />;
};

const mapStateToProps = state => ({
    checkMail: state.components.forms.register.checkMail
});

export default connect(mapStateToProps, {getCheckMail})(MailField);