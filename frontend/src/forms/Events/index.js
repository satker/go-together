import React, {useEffect, useState} from "react";

import GroupItems from "forms/utils/components/CardItems";
import {connect} from "App/Context";
import Container from "forms/utils/components/Container/ContainerRow";
import LoadableContent from "forms/utils/components/LoadableContent";
import {postLikes} from "forms/utils/components/Event/EventLikes/actions";
import CustomPagination from "forms/utils/components/Pagination";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import RightContainer from "forms/utils/components/Container/RightContainer";

import {postFindEvents} from "./actions";
import Filter from "./Filter";

const Events = ({pageSize, postFindEvents, findEvents, postLikes, filter}) => {
    const [page, setPage] = useState(1);

    useEffect(() => {
        filter.page.size = pageSize;
    }, [filter, pageSize]);

    useEffect(() => {
        if (findEvents.response.result && findEvents.response.result?.length) {
            const eventIds = findEvents.response.result.map(event => event.id);
            postLikes(eventIds);
        }
    }, [findEvents, postLikes]);

    useEffect(() => {
        postFindEvents(filter);
    }, [postFindEvents, filter]);

    const onClickNextPage = page => {
        filter.page.page = page - 1;
        setPage(page);
        postFindEvents(filter);
    };

    const onDelete = () => null;

    const pageCount = findEvents.response.page ?
        findEvents.response.page.totalSize / findEvents.response.page.size : 0;

    return <Container>
        <ContainerColumn>
            <LeftContainer style={{width: '20%'}}>
                <Filter/>
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
    filter: state.components.forms.events.filter.response
});

export default connect(mapStateToProps,
    {postFindEvents, postLikes})
(Events);