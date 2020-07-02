import React, {useState} from 'react';

import LabeledInput from "forms/utils/components/LabeledInput";

import {handleDescription} from "../validation";
import {GOOD_DESCRIPTION} from "../constants";

const DescriptionField = ({description, setDescription, setIsIncorrectData}) => {
    const [checkedDescription, setCheckedDescription] = useState(GOOD_DESCRIPTION);
    const [isDescriptionReadyForRegister, setIsDescriptionReadyForRegister] = useState(true);

    const checkResult = (isCorrect) => {
        setIsIncorrectData(isCorrect);
        setIsDescriptionReadyForRegister(isCorrect);
    };

    return <LabeledInput
        isError={!isDescriptionReadyForRegister}
        id="description"
        label="Description"
        errorText={checkedDescription}
        value={description}
        onChange={(value) => {
            setDescription(value);
            handleDescription(value, setCheckedDescription, checkResult);
        }}
    />;
};

export default DescriptionField;