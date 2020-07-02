import React, {useEffect, useState} from 'react';

import ItemContainer from "forms/utils/components/Container/ItemContainer";
import LabeledInput from "forms/utils/components/LabeledInput";

import {handleConfirmPassword, handlePassword} from "../validation";
import {EMPTY_PASSWORD} from "../constants";

const PasswordField = ({password, setPassword, confirmPassword, setConfirmPassword, setIsIncorrectData}) => {
    const [checkedPassword, setCheckedPassword] = useState(EMPTY_PASSWORD);
    const [isPasswordReadyForRegister, setIsPasswordReadyForRegister] = useState(false);
    const [checkedConfirmPassword, setCheckedConfirmPassword] = useState(EMPTY_PASSWORD);
    const [isConfirmPasswordReadyForRegister, setIsConfirmPasswordReadyForRegister] = useState(false);

    useEffect(() => {
        if (isPasswordReadyForRegister && isConfirmPasswordReadyForRegister) {
            setIsIncorrectData(true);
        } else {
            setIsIncorrectData(false);
        }
    }, [isPasswordReadyForRegister, isConfirmPasswordReadyForRegister, setIsIncorrectData]);

    return <>
        <ItemContainer>
            <LabeledInput
                isError={!isPasswordReadyForRegister}
                id="password"
                type='password'
                label="Password"
                errorText={checkedPassword}
                value={password}
                onChange={(value) => {
                    setPassword(value);
                    handlePassword(value, setCheckedPassword, setIsPasswordReadyForRegister);
                }}
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput id="confirmPassword"
                          type='password'
                          isError={!isConfirmPasswordReadyForRegister}
                          label="Reenter password"
                          errorText={checkedConfirmPassword}
                          value={confirmPassword}
                          onChange={(value) => {
                              setConfirmPassword(value);
                              handleConfirmPassword(value, setCheckedConfirmPassword,
                                  setIsConfirmPasswordReadyForRegister, password);
                          }}/>
        </ItemContainer>
    </>
};

export default PasswordField;