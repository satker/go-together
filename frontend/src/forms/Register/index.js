import React, {useEffect, useState} from 'react';
import '../../Form.css'
import {Button, Col, FormFeedback, Input, Label} from "reactstrap";
import createAutosuggestion from "../utils/components/Autosuggestion";
import {LOCATION_SERVICE_URL, USER_SERVICE_URL} from '../utils/constants'
import {registerFetch} from "../../App/utils/api/request";
import {connect} from "../../App/Context";
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
    FORM_ID,
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
import {getAllInterests, getAllLanguages, getCheckMail, getCheckUserName} from "./actions";

const URL = USER_SERVICE_URL + "/users";

const Autosuggestion = createAutosuggestion('AutosuggestionLocation');

const FormRegister = ({
                          allLanguages, allInterests, getAllInterests, getAllLanguages, getCheckMail, checkMail,
                          checkUserName, getCheckUserName
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

    return <Container>
        <ItemContainer>
            <Label for="login">Login</Label>
            <Input invalid={!isUserNameReadyForRegister} valid={isUserNameReadyForRegister}
                   type="text"
                   name="login" id="login" placeholder="login"
                   onChange={(evt) => {
                       handleChange(setLogin, evt);
                       handleUserName(evt, setCheckedUserName, setIsUserNameReadyForRegister, getCheckUserName);
                   }
                   }/>
            <FormFeedback invalid>{checkedUserName}</FormFeedback>
        </ItemContainer>
        <ItemContainer>
            <Label for="email">Email</Label>
            <Input invalid={!isMailReadyForRegister} valid={isMailReadyForRegister}
                   type="text" name="email" id="email" placeholder="email@email.com"
                   onChange={(evt) => {
                       handleChange(setMail, evt);
                       handleMail(evt, setCheckedMail, setIsMailReadyForRegister, getCheckMail);
                   }
                   }/>
            <FormFeedback invalid>{checkedMail}</FormFeedback>
        </ItemContainer>
        <ItemContainer>
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
        </ItemContainer>
        <ItemContainer>
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
        </ItemContainer>
        <ItemContainer>
            <Label for="location">Location</Label>
            <Autosuggestion
                formId='register_'
                setResult={(value) => setLocation(value)}
                placeholder={'Search a location (CITY,COUNTRY)'}
                url={LOCATION_SERVICE_URL + '/locations'}
                urlParam={'name'}
            />
        </ItemContainer>
        <ItemContainer>
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
        </ItemContainer>
        <ItemContainer><MultipleSelectBox label='Select languages'
                                          value={languages}
                                          optionsSimple={allLanguages.response}
                                          onChange={setLanguages}/>
        </ItemContainer>
        <ItemContainer>
            <MultipleSelectBox label='Select interests'
                               value={interests}
                               optionsSimple={allInterests.response}
                               onChange={setInterests}/>
        </ItemContainer>
        <ItemContainer>
            {userPhoto && <CardImg width={"20%"} src={getSrcForImg(userPhoto)}/>}
            <ImageSelector photos={userPhoto}
                           setPhotos={photo => {
                               handlePhoto(photo, setCheckedPhoto, setIsPhotoReadyForRegister);
                               setUserPhoto(photo)
                           }}
                           multiple={false}
            />
            <FormFeedback invalid>{checkedPhoto}</FormFeedback>
        </ItemContainer>
        <ItemContainer>
            <Label for="password" hidden>Password</Label>
            <Input invalid={!isPasswordReadyForRegister} valid={isPasswordReadyForRegister}
                   type="password" name="password" id="password" placeholder="Password"
                   onChange={(evt) => {
                       handleChange(setPassword, evt);
                       handlePassword(evt, setCheckedPassword, setIsPasswordReadyForRegister);
                   }}/>
            <FormFeedback invalid>{checkedPassword}</FormFeedback>
        </ItemContainer>

        <ItemContainer>
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
        </ItemContainer>
        <ItemContainer>
            <CustomReference action={moveToMainPage} description='Already registered?'/>
        </ItemContainer>
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
    </Container>;
};

const mapStateToProps = () => (state) => ({
    allLanguages: state[FORM_ID]?.languages || [],
    allInterests: state[FORM_ID]?.interests || [],
    checkMail: state[FORM_ID]?.checkMail,
    checkUserName: state[FORM_ID]?.checkUserName
});

export default connect(mapStateToProps,
    {getAllLanguages, getAllInterests, getCheckMail, getCheckUserName})(FormRegister)(FORM_ID);
