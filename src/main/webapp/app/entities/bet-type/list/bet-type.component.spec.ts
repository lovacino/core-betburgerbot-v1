import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BetTypeService } from '../service/bet-type.service';

import { BetTypeComponent } from './bet-type.component';

describe('BetType Management Component', () => {
  let comp: BetTypeComponent;
  let fixture: ComponentFixture<BetTypeComponent>;
  let service: BetTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'bet-type', component: BetTypeComponent }]),
        HttpClientTestingModule,
        BetTypeComponent,
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
      .overrideTemplate(BetTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BetTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BetTypeService);

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
    expect(comp.betTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to betTypeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getBetTypeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBetTypeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
