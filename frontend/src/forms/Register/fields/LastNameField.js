import React, {useState} from 'react';

import LabeledInput from "forms/utils/components/LabeledInput";

import {handleLastName} from "../validation";
import {EMPTY_LAST_NAME} from "../constants";

const LastNameField = ({lastName, setLastName, setIsIncorrectData}) => {
    const [checkedLastName, setCheckedLastName] = useState(EMPTY_LAST_NAME);
    const [isLastNameReadyForRegister, setIsLastNameReadyForRegister] = useState(false);

    const checkResult = (isCorrect) => {
        setIsIncorrectData(isCorrect);
        setIsLastNameReadyForRegister(isCorrect);
    };

    return <LabeledInput
        isError={!isLastNameReadyForRegister}
        id="lastName"
        label="Last name"
        errorText={checkedLastName}
        value={lastName}
        onChange={(value) => {
            setLastName(value);
            handleLastName(value, setCheckedLastName, checkResult);
        }}
    />;
}

export default LastNameField;