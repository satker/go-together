import React, {useEffect, useState} from "react";
import SearchForm from "./SearchForm";
import {Container} from "reactstrap";
import ReactPaginate from 'react-paginate';
import {FORM_DTO, SEARCH_OBJECT_DEFAULT} from '../utils/constants'
import './styles.css'
import {get, isEqual, set} from "lodash";
import GroupItems from "../utils/components/CardItems";
import {connect} from "../../App/Context";
import {postFindEvents, setEventId} from "./actions";
import {FORM_ID} from "./constants";

const Events = ({pageSize, postFindEvents, setEventId, findEvents}) => {
    const [events, setEvents] = useState([]);
    const [pageCount, setPageCount] = useState(0);
    const [searchObject, setSearchObject] = useState({...SEARCH_OBJECT_DEFAULT});
    const [filterObject, setFilterObject] = useState({...FORM_DTO("apartment.id")});

    useEffect(() => {
        filterObject.page.size = pageSize;
    }, [filterObject, pageSize]);

    useEffect(() => {
        if (findEvents.response.length !== 0) {
            const newEvents = {
                events: findEvents.response.result,
                page: filterObject.page.page

            };

            setEvents(newEvents);
            setPageCount(newEvents.page.totalSize / newEvents.page.size)
        }
    }, [setEvents, findEvents, filterObject]);

    useEffect(() => {
        postFindEvents(filterObject);
    }, [postFindEvents, filterObject]);

    const onClickChooseEvent = (event) => setEventId(event.id);

    const onClickNextPage = page => {
        filterObject.page.page = page.selected;
        postFindEvents(filterObject);
    };

    const onDelete = () => null;

    const onChangeSearchObject = (field, value) => {
        const object = {...searchObject};
        if (!isEqual(get(object, field), value)) {
            set(object, field, value);
            setSearchObject(object);
        }
    };

    const onClearSearchObject = () => setSearchObject({...SEARCH_OBJECT_DEFAULT});

    return <>
        <Container className='search-container'>
            <SearchForm setEvents={setEvents}
                        filterObject={filterObject}
                        setFilterObject={setFilterObject}
                        setPageCount={setPageCount}
                        searchObject={searchObject}
                        onChangeSearchObject={onChangeSearchObject}
                        onClearSearchObject={onClearSearchObject}
            />
        </Container>
        <Container className='events-container'>
            <GroupItems
                onDelete={onDelete}
                onClick={onClickChooseEvent}
                items={events.events}
                isEvents
            />
        </Container>
        <br/>
        {pageCount <= 1 || <Container sm="12" md={{size: 6, offset: 3}}>
            <div className="react-paginate">
                <ReactPaginate
                    previousLabel={"← Previous"}
                    nextLabel={"Next →"}
                    breakLabel={'...'}
                    breakClassName={'break-me'}
                    pageCount={pageCount}
                    marginPagesDisplayed={2}
                    pageRangeDisplayed={5}
                    onPageChange={onClickNextPage}
                    containerClassName={'pagination'}
                    subContainerClassName={'pages pagination'}
                    activeClassName={'active'}
                />
            </div>
        </Container>
        }
    </>;
};

const mapStateToProps = () => state => ({
    pageSize: state.pageSize,
    findEvents: state[FORM_ID]?.findEvents || [],
});

export default connect(mapStateToProps,
    {postFindEvents, setEventId})
(Events)(FORM_ID);