import React from "react";

import {connect} from "App/Context";
import CheckBox from "forms/utils/components/CheckBox";
import {setFilter} from "../actions";
import {FilterOperator} from "../../utils/utils";

const AUTHOR_ID = 'authorId';

const MyEvents = ({filter, setFilter, userId}) => {
    const authorId = filter.filters[AUTHOR_ID];

    const onChange = (value) => {
        if (value) {
            const values = [{
                [AUTHOR_ID]: {
                    filterType: FilterOperator.EQUAL.operator,
                    value: userId
                }
            }];
            setFilter(values, AUTHOR_ID);
        } else {
            setFilter([], AUTHOR_ID);
        }
    }

    return userId && <CheckBox value={!!authorId}
                               setValue={onChange}
                               label='My events'/>
};

const mapStateToProps = state => ({
    filter: state.components.forms.events.filter.response,
    userId: state.auth.response.userId
});

export default connect(mapStateToProps, {setFilter})(MyEvents);