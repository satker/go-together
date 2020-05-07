import {createEmptyResponse} from "../../App/utils/utils";
import {
    REGISTER_ALL_INTERESTS,
    REGISTER_ALL_LANGUAGES,
    REGISTER_CHECK_MAIL,
    REGISTER_CHECK_USERNAME
} from "./constants";

export const register = {
    allLanguages: createEmptyResponse(REGISTER_ALL_LANGUAGES, []),
    allInterests: createEmptyResponse(REGISTER_ALL_INTERESTS, []),
    checkMail: createEmptyResponse(REGISTER_CHECK_MAIL),
    checkUserName: createEmptyResponse(REGISTER_CHECK_USERNAME)
};