import React, {useContext, useEffect, useState} from 'react';
import '../Form.css'
import {Button, Col, FormFeedback, FormGroup, Input, Label} from "reactstrap";
import AutosuggestionLocation from "../utils/components/Autosuggestion";
import {LOCATION_SERVICE_URL, USER_SERVICE_URL} from '../utils/constants'
import {registerFetch} from "../utils/api/request";
import {Context} from "../Context";
import FormText from "reactstrap/es/FormText";
import {getSrcForImg} from "../utils/utils";
import CardImg from "reactstrap/es/CardImg";
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
    GOOD_PHOTO
} from "./constants";
import CustomReference from "../utils/components/CustomReference";
import MultipleSelectBox from "../utils/components/MultipleSelectBox";

const URL = USER_SERVICE_URL + "/users";
const URL_USERS_LANGUAGES = USER_SERVICE_URL + "/languages";
const URL_USERS_INTERESTS = USER_SERVICE_URL + "/interests";

const FormRegister = () => {
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
    const [languages, setLanguages] = useState([]);
    const [allLanguages, setAllLanguages] = useState([]);
    const [interests, setInterests] = useState([]);
    const [allInterests, setAllInterests] = useState([]);

    const [state] = useContext(Context);

    useEffect(() => {
        state.fetchWithToken(URL_USERS_LANGUAGES, setAllLanguages);
        state.fetchWithToken(URL_USERS_INTERESTS, setAllInterests);
    }, [state]);

    const handleChange = (set, evt) => set(evt.target.value);

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
        registerFetch(URL, () => '', body,
            () => alert("Failed to register. Check your data."),
            moveToMainPage
        );
    };

    const moveToMainPage = () => navigate('/');

    return <>
        <FormGroup>
            <Label for="login">Login</Label>
            <Input invalid={!isUserNameReadyForRegister} valid={isUserNameReadyForRegister}
                   type="text"
                   name="login" id="login" placeholder="login"
                   onChange={(evt) => {
                       handleChange(setLogin, evt);
                       handleUserName(evt, setCheckedUserName, setIsUserNameReadyForRegister, state.fetchWithToken);
                   }
                   }/>
            <FormFeedback invalid>{checkedUserName}</FormFeedback>
        </FormGroup>
        <FormGroup>
            <Label for="email">Email</Label>
            <Input invalid={!isMailReadyForRegister} valid={isMailReadyForRegister}
                   type="text" name="email" id="email" placeholder="email@email.com"
                   onChange={(evt) => {
                       handleChange(setMail, evt);
                       handleMail(evt, setCheckedMail, setIsMailReadyForRegister, state.fetchWithToken);
                   }
                   }/>
            <FormFeedback invalid>{checkedMail}</FormFeedback>
        </FormGroup>
        <FormGroup>
            <Label for="firstName">First name</Label>
            <Input invalid={!isFirstNameReadyForRegister} valid={isFirstNameReadyForRegister}
                   type="text" name="firstName" id="firstName" placeholder="John"
                   onChange={(evt) => {
                       handleChange(setFirstName, evt);
                       handleName(evt, setCheckedFirstName, setIsFirstNameReadyForRegister,
                           setCheckedLastName, setIsLastNameReadyForRegister);
                   }
                   }/>
            <FormFeedback invalid>{checkedFirstName}</FormFeedback>
        </FormGroup>
        <FormGroup>
            <Label for="lastName">Last name</Label>
            <Input invalid={!isLastNameReadyForRegister} valid={isLastNameReadyForRegister}
                   type="text" name="lastName" id="lastName" placeholder="Johnson"
                   onChange={(evt) => {
                       handleChange(setLastName, evt);
                       handleName(evt, setCheckedFirstName, setIsFirstNameReadyForRegister,
                           setCheckedLastName, setIsLastNameReadyForRegister);
                   }
                   }/>
            <FormFeedback invalid>{checkedLastName}</FormFeedback>
        </FormGroup>
        <FormGroup>
            <Label for="location">Location</Label>
            <AutosuggestionLocation
                formId='register_'
                setResult={(value) => setLocation(value)}
                placeholder={'Search a location (CITY,COUNTRY)'}
                url={LOCATION_SERVICE_URL + '/locations'}
                urlParam={'name'}
            />
        </FormGroup>
        <br/>
        <FormGroup row>
            <Label for="description" sm={2}>Description</Label>
            <Col sm={10}>
                <Input type="textarea" name="text" id="description"
                       placeholder="I like sea."
                       onChange={(evt) => {
                           handleChange(setDescription, evt);
                           handleDescription(evt, setCheckedDescription, setIsDescriptionReadyForRegister);
                       }}/>
            </Col>
            <FormFeedback invalid>{checkedDescription}</FormFeedback>
        </FormGroup>
        <MultipleSelectBox label='Select languages'
                           value={languages}
                           optionsSimple={allLanguages}
                           onChange={setLanguages}/>
        <MultipleSelectBox label='Select interests'
                           value={interests}
                           optionsSimple={allInterests}
                           onChange={setInterests}/>
        <FormGroup>
            {userPhoto && <CardImg width={"20%"} src={getSrcForImg(userPhoto)}/>}
            <ImageSelector photos={userPhoto}
                           setPhotos={photo => {
                               handlePhoto(photo, setCheckedPhoto, setIsPhotoReadyForRegister);
                               setUserPhoto(photo)
                           }}
                           multiple={false}
            />
            <FormFeedback invalid>{checkedPhoto}</FormFeedback>
        </FormGroup>
        <FormGroup>
            <Label for="password" hidden>Password</Label>
            <Input invalid={!isPasswordReadyForRegister} valid={isPasswordReadyForRegister}
                   type="password" name="password" id="password" placeholder="Password"
                   onChange={(evt) => {
                       handleChange(setPassword, evt);
                       handlePassword(evt, setCheckedPassword, setIsPasswordReadyForRegister);
                   }}/>
            <FormFeedback invalid>{checkedPassword}</FormFeedback>
        </FormGroup>

        <FormGroup>
            <Label for="confirmPassword" hidden>Confirm Password</Label>
            <Input invalid={!isConfirmPasswordReadyForRegister}
                   valid={isConfirmPasswordReadyForRegister}
                   type="password"
                   name="confirmPassword"
                   id="confirmPassword"
                   placeholder="Confirm password"
                   onChange={(evt) => handleConfirmPassword(evt, setCheckedConfirmPassword,
                       setIsConfirmPasswordReadyForRegister, password)}/>
            <FormFeedback invalid>{checkedConfirmPassword}</FormFeedback>
        </FormGroup>
        <FormText>
            <CustomReference action={moveToMainPage} description='Already registered?'/>
        </FormText>
        <Button className="btn btn-success"
                onClick={handleSubmit}
                disabled={!(isUserNameReadyForRegister &&
                    isMailReadyForRegister
                    && isFirstNameReadyForRegister &&
                    isLastNameReadyForRegister &&
                    isPasswordReadyForRegister &&
                    isConfirmPasswordReadyForRegister &&
                    isDescriptionReadyForRegister &&
                    isPhotoReadyForRegister
                )}>Register</Button>
        <Button color="danger" onClick={moveToMainPage}>Close</Button>
    </>;
};

export default FormRegister
