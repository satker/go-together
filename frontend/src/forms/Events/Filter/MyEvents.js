import React from "react";

import {connect} from "App/Context";
import CheckBox from "forms/utils/components/CheckBox";
import {setFilter} from "../actions";
import {FilterOperator} from "../../utils/utils";

const AUTHOR_ID = 'authorId';

const MyEvents = ({filter, setFilter, userId}) => {
    const authorId = filter.filters[AUTHOR_ID];
    console.log(userId)

    const onChange = (value) => {
        if (value) {
            const values = [{
                [AUTHOR_ID]: userId
            }];
            setFilter(FilterOperator.EQUAL, values, AUTHOR_ID);
        } else {
            setFilter(FilterOperator.EQUAL, [], AUTHOR_ID);
        }
    }

    return userId && <CheckBox value={!!authorId}
                               setValue={onChange}
                               label='My events'/>
};

const mapStateToProps = state => ({
    filter: state.components.forms.events.filter.response,
    userId: state.auth.value.userId
});

export default connect(mapStateToProps, {setFilter})(MyEvents);