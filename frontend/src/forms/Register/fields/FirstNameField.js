import React, {useState} from 'react';

import LabeledInput from "forms/utils/components/LabeledInput";

import {handleFirstName} from "../validation";
import {EMPTY_FIRST_NAME} from "../constants";

const FirstNameField = ({firstName, setFirstName, setIsIncorrectData}) => {
    const [checkedFirstName, setCheckedFirstName] = useState(EMPTY_FIRST_NAME);
    const [isFirstNameReadyForRegister, setIsFirstNameReadyForRegister] = useState(false);

    const checkResult = (isCorrect) => {
        setIsIncorrectData(isCorrect);
        setIsFirstNameReadyForRegister(isCorrect);
    };

    return <LabeledInput
        isError={!isFirstNameReadyForRegister}
        id="firstName"
        label="First name"
        errorText={checkedFirstName}
        value={firstName}
        onChange={(value) => {
            setFirstName(value);
            handleFirstName(value, setCheckedFirstName, checkResult);
        }}
    />
}

export default FirstNameField;