import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IHotnews } from 'app/shared/model/hotnews.model';

export const ACTION_TYPES = {
  SEARCH_HOTNEWS: 'hotnews/SEARCH_HOTNEWS',
  FETCH_HOTNEWS_LIST: 'hotnews/FETCH_HOTNEWS_LIST',
  FETCH_HOTNEWS: 'hotnews/FETCH_HOTNEWS',
  CREATE_HOTNEWS: 'hotnews/CREATE_HOTNEWS',
  UPDATE_HOTNEWS: 'hotnews/UPDATE_HOTNEWS',
  DELETE_HOTNEWS: 'hotnews/DELETE_HOTNEWS',
  RESET: 'hotnews/RESET'
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
    case REQUEST(ACTION_TYPES.SEARCH_HOTNEWS):
    case REQUEST(ACTION_TYPES.FETCH_HOTNEWS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HOTNEWS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HOTNEWS):
    case REQUEST(ACTION_TYPES.UPDATE_HOTNEWS):
    case REQUEST(ACTION_TYPES.DELETE_HOTNEWS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_HOTNEWS):
    case FAILURE(ACTION_TYPES.FETCH_HOTNEWS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HOTNEWS):
    case FAILURE(ACTION_TYPES.CREATE_HOTNEWS):
    case FAILURE(ACTION_TYPES.UPDATE_HOTNEWS):
    case FAILURE(ACTION_TYPES.DELETE_HOTNEWS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_HOTNEWS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOTNEWS_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOTNEWS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HOTNEWS):
    case SUCCESS(ACTION_TYPES.UPDATE_HOTNEWS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HOTNEWS):
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

const apiUrl = SERVER_API_URL + '/api/hotnews';
const apiSearchUrl = SERVER_API_URL + '/api/_search/hotnews';

// Actions

export const getSearchEntities: ICrudSearchAction<IHotnews> = query => ({
  type: ACTION_TYPES.SEARCH_HOTNEWS,
  payload: axios.get(`${apiSearchUrl}?query=` + query) as Promise<IHotnews>
});

export const getEntities: ICrudGetAllAction<IHotnews> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HOTNEWS_LIST,
    payload: axios.get(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`) as Promise<IHotnews>
  };
};

export const getEntity: ICrudGetAction<IHotnews> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HOTNEWS,
    payload: axios.get(requestUrl) as Promise<IHotnews>
  };
};

export const createEntity: ICrudPutAction<IHotnews> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HOTNEWS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHotnews> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HOTNEWS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHotnews> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HOTNEWS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
