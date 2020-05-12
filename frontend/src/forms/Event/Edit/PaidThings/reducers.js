import {createEmptyResponse} from "../../../../App/utils/utils";
import {PAID_THINGS, PAID_THINGS_CASH_CATEGORIES} from "./constants";

export const paidThings = {
    cashCategories: createEmptyResponse(PAID_THINGS_CASH_CATEGORIES, []),
    payedThings: createEmptyResponse(PAID_THINGS, [])
};