import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IGoods } from 'app/shared/model/goods.model';

export const ACTION_TYPES = {
  SEARCH_GOODS: 'goods/SEARCH_GOODS',
  FETCH_GOODS_LIST: 'goods/FETCH_GOODS_LIST',
  FETCH_GOODS: 'goods/FETCH_GOODS',
  CREATE_GOODS: 'goods/CREATE_GOODS',
  UPDATE_GOODS: 'goods/UPDATE_GOODS',
  DELETE_GOODS: 'goods/DELETE_GOODS',
  RESET: 'goods/RESET'
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
    case REQUEST(ACTION_TYPES.SEARCH_GOODS):
    case REQUEST(ACTION_TYPES.FETCH_GOODS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GOODS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GOODS):
    case REQUEST(ACTION_TYPES.UPDATE_GOODS):
    case REQUEST(ACTION_TYPES.DELETE_GOODS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_GOODS):
    case FAILURE(ACTION_TYPES.FETCH_GOODS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GOODS):
    case FAILURE(ACTION_TYPES.CREATE_GOODS):
    case FAILURE(ACTION_TYPES.UPDATE_GOODS):
    case FAILURE(ACTION_TYPES.DELETE_GOODS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_GOODS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GOODS_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GOODS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GOODS):
    case SUCCESS(ACTION_TYPES.UPDATE_GOODS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GOODS):
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

const apiUrl = SERVER_API_URL + '/api/goods';
const apiSearchUrl = SERVER_API_URL + '/api/_search/goods';

// Actions

export const getSearchEntities: ICrudSearchAction<IGoods> = query => ({
  type: ACTION_TYPES.SEARCH_GOODS,
  payload: axios.get(`${apiSearchUrl}?query=` + query) as Promise<IGoods>
});

export const getEntities: ICrudGetAllAction<IGoods> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GOODS_LIST,
    payload: axios.get(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`) as Promise<IGoods>
  };
};

export const getEntity: ICrudGetAction<IGoods> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GOODS,
    payload: axios.get(requestUrl) as Promise<IGoods>
  };
};

export const createEntity: ICrudPutAction<IGoods> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GOODS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGoods> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GOODS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGoods> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GOODS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
