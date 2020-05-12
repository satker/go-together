import React, {useEffect, useState} from 'react';
import {AutosuggestionLocations} from "../utils/components/Autosuggestion";
import {LOCATION_SERVICE_URL} from '../utils/constants'
import {connect} from "../../App/Context";
import {getSrcForImg} from "../utils/utils";
import {navigate} from 'hookrouter';
import ImageSelector from "../utils/components/ImageSelector";
import {
    handleConfirmPassword,
    handleDescription,
    handleMail,
    handleName,
    handlePassword,
    handlePhoto,
    handleUserName,
} from "./validation";
import {
    EMPTY_FIRST_NAME,
    EMPTY_LAST_NAME,
    EMPTY_LOGIN,
    EMPTY_MAIL,
    EMPTY_PASSWORD,
    GOOD_DESCRIPTION,
    GOOD_LOGIN,
    GOOD_MAIL,
    GOOD_PHOTO,
    NOT_GOOD_LOGIN,
    NOT_GOOD_MAIL
} from "./constants";
import CustomReference from "../utils/components/CustomReference";
import MultipleSelectBox from "../utils/components/MultipleSelectBox";
import Container from "../utils/components/Container/ContainerRow";
import ItemContainer from "../utils/components/Container/ItemContainer";
import {getAllInterests, getAllLanguages, getCheckMail, getCheckUserName, registerUser} from "./actions";
import CardMedia from "@material-ui/core/CardMedia";
import ErrorMessage from "../utils/components/LoadableContent/ErrorMessage";
import LoadableContent from "../utils/components/LoadableContent";
import LabeledInput from "../utils/LabeledInput";
import CustomButton from "../utils/components/CustomButton";

const FormRegister = ({
                          allLanguages, allInterests, getAllInterests, getAllLanguages, getCheckMail, checkMail,
                          checkUserName, getCheckUserName, registerUser, registeredUser
                      }) => {
    const [checkedUserName, setCheckedUserName] = useState(EMPTY_LOGIN);
    const [isUserNameReadyForRegister, setIsUserNameReadyForRegister] = useState(false);
    const [checkedMail, setCheckedMail] = useState(EMPTY_MAIL);
    const [isMailReadyForRegister, setIsMailReadyForRegister] = useState(false);
    const [checkedFirstName, setCheckedFirstName] = useState(EMPTY_FIRST_NAME);
    const [isFirstNameReadyForRegister, setIsFirstNameReadyForRegister] = useState(false);
    const [checkedLastName, setCheckedLastName] = useState(EMPTY_LAST_NAME);
    const [isLastNameReadyForRegister, setIsLastNameReadyForRegister] = useState(false);
    const [checkedPassword, setCheckedPassword] = useState(EMPTY_PASSWORD);
    const [isPasswordReadyForRegister, setIsPasswordReadyForRegister] = useState(false);
    const [checkedConfirmPassword, setCheckedConfirmPassword] = useState(EMPTY_PASSWORD);
    const [isConfirmPasswordReadyForRegister, setIsConfirmPasswordReadyForRegister] = useState(false);
    const [checkedDescription, setCheckedDescription] = useState(GOOD_DESCRIPTION);
    const [isDescriptionReadyForRegister, setIsDescriptionReadyForRegister] = useState(true);
    const [checkedPhoto, setCheckedPhoto] = useState(GOOD_PHOTO);
    const [isPhotoReadyForRegister, setIsPhotoReadyForRegister] = useState(true);

    const [login, setLogin] = useState(null);
    const [mail, setMail] = useState(null);
    const [firstName, setFirstName] = useState(null);
    const [lastName, setLastName] = useState(null);
    const [location, setLocation] = useState(null);
    const [description, setDescription] = useState(null);
    const [userPhoto, setUserPhoto] = useState(null);
    const [password, setPassword] = useState(null);
    const [confirmPassword, setConfirmPassword] = useState(null);
    const [languages, setLanguages] = useState([]);
    const [interests, setInterests] = useState([]);

    useEffect(() => {
        getAllInterests();
    }, [getAllInterests]);

    useEffect(() => {
        getAllLanguages();
    }, [getAllLanguages]);

    useEffect(() => {
        if (checkMail.response === true) {
            setCheckedMail(NOT_GOOD_MAIL);
            setIsMailReadyForRegister(false);
        } else {
            setCheckedMail(GOOD_MAIL);
            setIsMailReadyForRegister(true);
        }
    }, [checkMail, setCheckedMail, setIsMailReadyForRegister]);

    useEffect(() => {
        if (checkUserName.response === true) {
            setCheckedUserName(NOT_GOOD_LOGIN);
            setIsUserNameReadyForRegister(false);
        } else {
            setCheckedUserName(GOOD_LOGIN);
            setIsUserNameReadyForRegister(true);
        }
    }, [checkUserName, setCheckedUserName, setIsUserNameReadyForRegister]);

    const handleSubmit = () => {
        let body = {};
        body.login = login;
        body.mail = mail;
        body.firstName = firstName;
        body.lastName = lastName;
        body.location = location;
        body.description = description;
        body.userPhotos = [userPhoto];//
        body.password = password;
        body.languages = languages.map(lang => ({id: lang.value, name: lang.label}));
        body.interests = interests.map(interest => ({id: interest.value, name: interest.label}));
        registerUser(body);
    };

    useEffect(() => {
        if (registeredUser.id) {
            moveToMainPage()
        }
    }, [registeredUser]);

    const moveToMainPage = () => navigate('/');

    return <Container>
        <ItemContainer>
            <LabeledInput
                isError={!isUserNameReadyForRegister}
                id="login"
                label="Login"
                errorText={checkedUserName}
                value={login}
                onChange={(value) => {
                    setLogin(value);
                    handleUserName(value, setCheckedUserName, setIsUserNameReadyForRegister, getCheckUserName);
                }
                }
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                isError={!isMailReadyForRegister}
                id="Email"
                label="Email"
                errorText={checkedMail}
                value={mail}
                onChange={(value) => {
                    setMail(value);
                    handleMail(value, setCheckedMail, setIsMailReadyForRegister, getCheckMail);
                }
                }
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                isError={!isFirstNameReadyForRegister}
                id="firstName"
                label="First name"
                errorText={checkedFirstName}
                value={firstName}
                onChange={(value) => {
                    setFirstName(value);
                    handleName(value, 'firstName', setCheckedFirstName, setIsFirstNameReadyForRegister,
                        setCheckedLastName, setIsLastNameReadyForRegister);
                }}
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                isError={!isLastNameReadyForRegister}
                id="lastName"
                label="Last name"
                errorText={checkedLastName}
                value={lastName}
                onChange={(value) => {
                    setLastName(value);
                    handleName(value, 'lastName', setCheckedFirstName, setIsFirstNameReadyForRegister,
                        setCheckedLastName, setIsLastNameReadyForRegister);
                }}
            />
        </ItemContainer>
        <ItemContainer>
            <AutosuggestionLocations
                formId='register_'
                setResult={(value) => setLocation(value)}
                placeholder={'Search a location (CITY,COUNTRY)'}
                url={LOCATION_SERVICE_URL + '/locations'}
                urlParam={'name'}
            />
        </ItemContainer>
        <ItemContainer>
            <LabeledInput
                isError={!isDescriptionReadyForRegister}
                id="description"
                label="Description"
                errorText={checkedDescription}
                value={description}
                onChange={(value) => {
                    setDescription(value);
                    handleDescription(value, setCheckedDescription, setIsDescriptionReadyForRegister);
                }}
            />
        </ItemContainer>
        <ItemContainer>
            <LoadableContent loadableData={allLanguages}>
                <MultipleSelectBox label='Select languages'
                                   value={languages}
                                   optionsSimple={allLanguages.response}
                                   onChange={setLanguages}/>
            </LoadableContent>
        </ItemContainer>
        <ItemContainer>
            <LoadableContent loadableData={allInterests}>
                <MultipleSelectBox label='Select interests'
                                   value={interests}
                                   optionsSimple={allInterests.response}
                                   onChange={setInterests}/>
            </LoadableContent>
        </ItemContainer>
        <ItemContainer>
            {userPhoto && <CardMedia img={getSrcForImg(userPhoto)}/>}
            <ImageSelector photos={userPhoto}
                           setPhotos={photo => {
                               handlePhoto(photo, setCheckedPhoto, setIsPhotoReadyForRegister);
                               setUserPhoto(photo)
                           }}
                           multiple={false}
            />
            <ErrorMessage error={checkedPhoto}/>
        </ItemContainer>
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
        <ItemContainer>
            <CustomReference action={moveToMainPage} description='Already registered?'/>
        </ItemContainer>
        <ItemContainer>
            <CustomButton color="primary"
                          onClick={handleSubmit}
                          disabled={!(isUserNameReadyForRegister &&
                              isMailReadyForRegister
                              && isFirstNameReadyForRegister &&
                              isLastNameReadyForRegister &&
                              isPasswordReadyForRegister &&
                              isConfirmPasswordReadyForRegister &&
                              isDescriptionReadyForRegister &&
                              isPhotoReadyForRegister
                          )}
                          text='Register'/>
        </ItemContainer>
        <ItemContainer>
            <CustomButton color="secondary" onClick={moveToMainPage} text='Close'/>
        </ItemContainer>
    </Container>;
};

const mapStateToProps = () => (state) => ({
    allLanguages: state.components.forms.register.allLanguages,
    allInterests: state.components.forms.register.allInterests,
    checkMail: state.components.forms.register.checkMail,
    checkUserName: state.components.forms.register.checkUserName,
    registeredUser: state.components.forms.register.registeredUser
});

export default connect(mapStateToProps,
    {getAllLanguages, getAllInterests, getCheckMail, getCheckUserName, registerUser})(FormRegister);
