import React, {useEffect, useState} from 'react';
import * as PropTypes from "prop-types";

import {connect} from "App/Context";

import {SearchObject} from "forms/utils/types";
import {getInterests, getLanguages} from "../actions";
import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {FilterOperator} from "forms/utils/utils";

const AuthorFilters = ({
                           updateFilterObject,
                           getInterests, getLanguages,
                           interests, languages
                       }) => {

    const [chooseInterests, setChooseInterests] = useState([]);
    const [chooseLanguages, setChooseLanguages] = useState([]);

    useEffect(() => {
        getInterests();
        getLanguages();
    }, [getLanguages, getInterests]);

    const onChangeFilterObject = (field, set) => (values) => {
        const searchField = "author?" + field + ".id";
        updateFilterObject(FilterOperator.IN, values, searchField);
        set(values);
    }

    return <>
        <LoadableContent loadableData={interests}>
            <MultipleSelectBox onChange={onChangeFilterObject('interests', setChooseInterests)}
                               label='Interests'
                               optionsSimple={interests.response}
                               value={chooseInterests}/>
        </LoadableContent>
        <LoadableContent loadableData={languages}>
            <MultipleSelectBox onChange={onChangeFilterObject('languages', setChooseLanguages)}
                               value={chooseLanguages}
                               optionsSimple={languages.response}
                               label='Languages'/>
        </LoadableContent>
    </>

};

AuthorFilters.propTypes = {
    focus: PropTypes.bool.isRequired,
    setFocus: PropTypes.func.isRequired,
    onChangeSearchObject: PropTypes.func.isRequired,
    searchObject: SearchObject.isRequired,
    getInterests: PropTypes.func.isRequired,
    getLanguages: PropTypes.func.isRequired,
    interests: PropTypes.array,
    languages: PropTypes.array,
    updateFilterObject: PropTypes.func.isRequired,
};

const mapStateToProps = () => state => ({
    interests: state.components.forms.events.interests,
    languages: state.components.forms.events.languages
});

export default connect(mapStateToProps, {getInterests, getLanguages})(AuthorFilters);