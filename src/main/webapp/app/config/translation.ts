/* global: require*/
import { TranslatorContext, Storage } from 'react-jhipster';
import { merge } from 'lodash';

import { setLocale } from 'app/shared/reducers/locale';

const mergeTranslations = requireContext =>
  requireContext.keys().reduce((merged, key) => merge({ ...merged }, { ...requireContext(key) }), {});

// tslint:disable:object-literal-key-quotes
const translations = {
  'zh-cn': mergeTranslations(require.context('../../i18n/zh-cn', false, /.json$/)),
  en: mergeTranslations(require.context('../../i18n/en', false, /.json$/))
};
// tslint:enable

let currentLocale;
const savedLocale = Storage.local.get('locale', 'zh-cn');
TranslatorContext.setDefaultLocale('zh-cn');
TranslatorContext.setRenderInnerTextForMissingKeys(false);

export const locales = Object.keys(translations);

export const registerLocales = store => {
  locales.forEach(key => {
    TranslatorContext.registerTranslations(key, translations[key]);
  });
  store.subscribe(() => {
    const previousLocale = currentLocale;
    currentLocale = store.getState().locale.currentLocale;
    if (previousLocale !== currentLocale) {
      Storage.local.set('locale', currentLocale);
      TranslatorContext.setLocale(currentLocale);
    }
  });
  store.dispatch(setLocale(savedLocale));
  return savedLocale;
};
