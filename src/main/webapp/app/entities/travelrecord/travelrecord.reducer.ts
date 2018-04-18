import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ITravelrecord } from 'app/shared/model/travelrecord.model';

export const ACTION_TYPES = {
  SEARCH_TRAVELRECORDS: 'travelrecord/SEARCH_TRAVELRECORDS',
  FETCH_TRAVELRECORD_LIST: 'travelrecord/FETCH_TRAVELRECORD_LIST',
  FETCH_TRAVELRECORD: 'travelrecord/FETCH_TRAVELRECORD',
  CREATE_TRAVELRECORD: 'travelrecord/CREATE_TRAVELRECORD',
  UPDATE_TRAVELRECORD: 'travelrecord/UPDATE_TRAVELRECORD',
  DELETE_TRAVELRECORD: 'travelrecord/DELETE_TRAVELRECORD',
  RESET: 'travelrecord/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: {},
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

// Reducer

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TRAVELRECORDS):
    case REQUEST(ACTION_TYPES.FETCH_TRAVELRECORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TRAVELRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TRAVELRECORD):
    case REQUEST(ACTION_TYPES.UPDATE_TRAVELRECORD):
    case REQUEST(ACTION_TYPES.DELETE_TRAVELRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_TRAVELRECORDS):
    case FAILURE(ACTION_TYPES.FETCH_TRAVELRECORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TRAVELRECORD):
    case FAILURE(ACTION_TYPES.CREATE_TRAVELRECORD):
    case FAILURE(ACTION_TYPES.UPDATE_TRAVELRECORD):
    case FAILURE(ACTION_TYPES.DELETE_TRAVELRECORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TRAVELRECORDS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TRAVELRECORD_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TRAVELRECORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TRAVELRECORD):
    case SUCCESS(ACTION_TYPES.UPDATE_TRAVELRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TRAVELRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVER_API_URL + '/api/travelrecords';
const apiSearchUrl = SERVER_API_URL + '/api/_search/travelrecords';

// Actions

export const getSearchEntities: ICrudSearchAction<ITravelrecord> = query => ({
  type: ACTION_TYPES.SEARCH_TRAVELRECORDS,
  payload: axios.get(`${apiSearchUrl}?query=` + query) as Promise<ITravelrecord>
});

export const getEntities: ICrudGetAllAction<ITravelrecord> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TRAVELRECORD_LIST,
    payload: axios.get(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`) as Promise<ITravelrecord>
  };
};

export const getEntity: ICrudGetAction<ITravelrecord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TRAVELRECORD,
    payload: axios.get(requestUrl) as Promise<ITravelrecord>
  };
};

export const createEntity: ICrudPutAction<ITravelrecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TRAVELRECORD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITravelrecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TRAVELRECORD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITravelrecord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TRAVELRECORD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
