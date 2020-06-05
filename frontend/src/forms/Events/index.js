import React, {useEffect, useState} from "react";

import {FORM_DTO} from 'forms/utils/constants'
import GroupItems from "forms/utils/components/CardItems";
import {connect} from "App/Context";
import Container from "forms/utils/components/Container/ContainerRow";
import LoadableContent from "forms/utils/components/LoadableContent";
import {postLikes} from "forms/utils/components/Event/EventLikes/actions";
import CustomPagination from "forms/utils/components/Pagination";
import {updateFormDto} from "forms/utils/utils";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import RightContainer from "forms/utils/components/Container/RightContainer";

import {postFindEvents} from "./actions";
import Filter from "./Filter";

const Events = ({pageSize, postFindEvents, findEvents, postLikes}) => {
    const [page, setPage] = useState(1);
    const [filterObject, setFilterObject] = useState({...FORM_DTO("event")});

    useEffect(() => {
        filterObject.page.size = pageSize;
    }, [filterObject, pageSize]);

    useEffect(() => {
        if (findEvents.response.result && findEvents.response.result?.length) {
            const eventIds = findEvents.response.result.map(event => event.id);
            postLikes(eventIds);
        }
    }, [findEvents, postLikes]);

    useEffect(() => {
        postFindEvents(filterObject);
    }, [postFindEvents, filterObject]);

    const onClickNextPage = page => {
        filterObject.page.page = page - 1;
        setPage(page);
        postFindEvents(filterObject);
    };

    const onDelete = () => null;

    const pageCount = findEvents.response.page ?
        findEvents.response.page.totalSize / findEvents.response.page.size : 0;

    return <Container>
        <ContainerColumn>
            <LeftContainer style={{width: '20%'}}>
                <Filter updateFilterObject={updateFormDto(filterObject, setFilterObject)}
                        filterObject={filterObject}
                />
            </LeftContainer>
            <RightContainer style={{width: '80%'}}>
                <LoadableContent loadableData={findEvents}>
                    <GroupItems
                        onDelete={onDelete}
                        items={findEvents.response.result}
                        isEvents
                    />
                </LoadableContent>
                {pageCount <= 1 || <CustomPagination pageCount={pageCount}
                                                     page={page}
                                                     setPage={onClickNextPage}/>}
            </RightContainer>
        </ContainerColumn>
    </Container>;
};

const mapStateToProps = () => state => ({
    pageSize: state.pageSize.value,
    findEvents: state.components.forms.events.findEvents,
});

export default connect(mapStateToProps,
    {postFindEvents, postLikes})
(Events);