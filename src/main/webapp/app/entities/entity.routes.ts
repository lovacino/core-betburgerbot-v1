import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'bookmaker',
    data: { pageTitle: 'betBurgerBotApp.bookmaker.home.title' },
    loadChildren: () => import('./bookmaker/bookmaker.routes'),
  },
  {
    path: 'account-bet',
    data: { pageTitle: 'betBurgerBotApp.accountBet.home.title' },
    loadChildren: () => import('./account-bet/account-bet.routes'),
  },
  {
    path: 'config-param',
    data: { pageTitle: 'betBurgerBotApp.configParam.home.title' },
    loadChildren: () => import('./config-param/config-param.routes'),
  },
  {
    path: 'sport',
    data: { pageTitle: 'betBurgerBotApp.sport.home.title' },
    loadChildren: () => import('./sport/sport.routes'),
  },
  {
    path: 'bet-type',
    data: { pageTitle: 'betBurgerBotApp.betType.home.title' },
    loadChildren: () => import('./bet-type/bet-type.routes'),
  },
  {
    path: 'period',
    data: { pageTitle: 'betBurgerBotApp.period.home.title' },
    loadChildren: () => import('./period/period.routes'),
  },
  {
    path: 'event-source',
    data: { pageTitle: 'betBurgerBotApp.eventSource.home.title' },
    loadChildren: () => import('./event-source/event-source.routes'),
  },
  {
    path: 'betting',
    data: { pageTitle: 'betBurgerBotApp.betting.home.title' },
    loadChildren: () => import('./betting/betting.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
