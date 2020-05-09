import {createEmptyResponse} from "../../App/utils/utils";
import {
    CREATED_USER,
    REGISTER_ALL_INTERESTS,
    REGISTER_ALL_LANGUAGES,
    REGISTER_CHECK_MAIL,
    REGISTER_CHECK_USERNAME
} from "./constants";

export const register = {
    registeredUser: createEmptyResponse(CREATED_USER),
    allLanguages: createEmptyResponse(REGISTER_ALL_LANGUAGES, []),
    allInterests: createEmptyResponse(REGISTER_ALL_INTERESTS, []),
    checkMail: createEmptyResponse(REGISTER_CHECK_MAIL),
    checkUserName: createEmptyResponse(REGISTER_CHECK_USERNAME)
};