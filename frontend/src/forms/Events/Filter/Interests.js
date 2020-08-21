import React, {useEffect} from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {FilterOperator} from "forms/utils/utils";

import {getInterests, setFilter} from "../actions";
import {ResponseData} from "forms/utils/types";
import {keys} from "lodash";

const INTERESTS_FIELD = "author?interests.id";

const Interests = ({interests, getInterests, setFilter, chooseInterests}) => {
    useEffect(() => {
        getInterests();
    }, [getInterests]);

    const onChange = (interests) => {
        let searchInterests = null;
        if (interests.length !== 0) {
            searchInterests = [{
                id: interests.map(interest => interest.id)
            }];
        }
        setFilter(FilterOperator.IN, searchInterests, INTERESTS_FIELD, interests.length);
    }

    return <LoadableContent loadableData={interests}>
        <MultipleSelectBox onChange={onChange}
                           label='Interests'
                           optionsSimple={interests.response.result}
                           value={chooseInterests}/>
    </LoadableContent>;
};

Interests.propTypes = {
    setFilter: PropTypes.func.isRequired,
    getInterests: PropTypes.func.isRequired,
    interests: ResponseData
}

const mapStateToProps = state => {
    const interests = state.components.forms.events.interests;
    const filters = state.components.forms.events.filter.response.filters;
    const filtersName = keys(filters)
        .find(keyFilter => keyFilter.startsWith(INTERESTS_FIELD));
    const filterInterests = filters[filtersName]?.values[0].id || [];
    const chooseInterests = filterInterests
        .map(interestId => ({
            id: interestId,
            name: interests.response?.result.find(interest => interest.id === interestId)
        }).name)

    return ({
        interests,
        chooseInterests
    });
}

export default connect(mapStateToProps, {getInterests, setFilter})(Interests);