import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccountBetService } from '../service/account-bet.service';

import { AccountBetComponent } from './account-bet.component';

describe('AccountBet Management Component', () => {
  let comp: AccountBetComponent;
  let fixture: ComponentFixture<AccountBetComponent>;
  let service: AccountBetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'account-bet', component: AccountBetComponent }]),
        HttpClientTestingModule,
        AccountBetComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(AccountBetComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountBetComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AccountBetService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.accountBets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to accountBetService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAccountBetIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAccountBetIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
