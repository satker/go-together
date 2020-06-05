import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";

import {SearchObject} from "forms/utils/types";
import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {FilterOperator} from "forms/utils/utils";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {getInterests, getLanguages} from "../actions";

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
        const id = 'id';
        const searchField = "author?" + field + "." + id;
        let newValues = null;
        if (values) {
            newValues = values.map(value => ({
                [id]: value[id]
            }));
        }
        updateFilterObject(FilterOperator.EQUAL, newValues, searchField);
        set(values);
    }

    return <>
        <ItemContainer>
            <LoadableContent loadableData={interests}>
                <MultipleSelectBox onChange={onChangeFilterObject('interests', setChooseInterests)}
                                   label='Interests'
                                   optionsSimple={interests.response}
                                   value={chooseInterests}/>
            </LoadableContent>
        </ItemContainer>
        <ItemContainer>
            <LoadableContent loadableData={languages}>
                <MultipleSelectBox onChange={onChangeFilterObject('languages', setChooseLanguages)}
                                   value={chooseLanguages}
                                   optionsSimple={languages.response}
                                   label='Languages'/>
            </LoadableContent>
        </ItemContainer>
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