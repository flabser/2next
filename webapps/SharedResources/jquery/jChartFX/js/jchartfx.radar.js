(function(){var n=window.cfx,f=window.sfx,G=function a(){a._ic();this.d=this.e=null;this.a=0};n.bA=G;G.prototype={_0bA:function(){this.k();return this},getCircular:function(){return 0!=(this.a&4)},setCircular:function(a){this.b(4,a)},getClockwise:function(){return 0!=(this.a&32)},setClockwise:function(a){this.b(32,a)},getClosed:function(){return 0!=(this.a&8)},setClosed:function(a){this.b(8,a)},getFillArea:function(){return 0!=(this.a&2)},setFillArea:function(a){this.b(2,a)},getShadows:function(){return 0!=
(this.a&16)},setShadows:function(a){this.b(16,a)},getShowLines:function(){return 0!=(this.a&1)},setShowLines:function(a){this.b(1,a)},icV:function(){return 1},icW:function(){return 8397382},ieG:function(a){this.e=a},icU:function(a,c,b){switch(c){case 11:return this.j(b)}return null},icX:function(a,c,b){a.a=1;a.b=0;var c=b.a.b.iaH(),k=b.v._nc();if(b.j.getVisible()){var m=new f.g,l=b.j.getLabelsCore();if(0!=l.Se()){var r=new f._p1(!1),l=l.id$(r,b.c,b.j.getFont(),!1);m._cf(l)}else l=f.b.j(b.bb,"{0}",
c),m._cf(b.c.idV(l,b.j.getFont()).c());k.m(-m.w,-m.h)}var j,e=new f._pN(0,0,0,0);n.bR.H(e,k,!0);l=e.a;r=e.b;m=e.c;j=e.d;m=f.a.n(m,1);j=f.a.n(j,1);var e=0!=(b.t&2),h=360/c,d=0,d=b.j.ar()?e?45:h:b.j.getStep();this.getClockwise()||(h=-h,d=-d);k=this.o(b,l,r,m,j,k,d);if(G.m(b,l,r,m,j,d,k,this.getCircular())){var i=e?0:-90;j=k=!1;for(var g=d=0,s=b.p;s<=b.q;s++)if(b.d=s,b.aI(s),!b.g(8192)){var x=!1,v=0,t=0,B=0,w=0,D=0,u=0,y=0,E=b.I,z=b.F,J=m/(z-E);this.d=null;for(var A=0;A<c;A++){b.e=A;b.aw();b.N(!0);j=
k;var o=b.u;e&&(i=this.getClockwise()?b.aT:-b.aT);i=new f._p1(i);n.bU.e(i);i=i.a;if(n.c6.d(o))if(0==A)x=!0;else{if(!n.c6.d(y)){var g=new f._p2(d,g),p=this.c(g,b,B,w,v,t,s,A,A-1,!0,l,r),d=g.a,g=g.b;if(!p&&(k=!0,0==(b.t&1)&&(j=!0),j))break}}else{if(o<=z&&o>=E){var o=o-E,F=p=0,F=p=o*J,t=new f._p2(v,t);n.bU.d(t,i,p,F,l,r);v=t.a;t=t.b;if(0!=A&&!n.c6.d(y)){if(g=new f._p2(d,g),B=this.c(g,b,B,w,v,t,s,A,A-1,!1,l,r),d=g.a,g=g.b,!B&&(k=!0,0==(b.t&1)&&(j=!0),j))break}else 0==A&&(D=v,u=t)}B=v;w=t;if(j)break}y=
o;i+=h;b.an(0,0)}if(j)break;if(!n.c6.d(y)){b.R=!1;b.aw();b.N(!0);this.getClosed()||(D=v,u=t);y=new f._p2(d,g);v=this.c(y,b,v,t,D,u,s,b.e,b.e,x,l,r);d=y.a;g=y.b;if(!v){k=!0;break}b.an(0,0);!x&&0!=(this.a&8)&&(b.e=0,b.R=!1,b.aw(),b.N(!0),x=new f._p2(d,g),this.c(x,b,D,u,D,u,s,0,0,!0,l,r),d=x.a,g=x.b,b.an(0,0))}}k?(b.d=d,b.e=g,a.b=0,a.a=a.b):(b.ak-=c-1,b.e=b.o)}},o:function(a,c,b,k,m,l,r){var j=a.j,e=a.w(512),h=f.e._ca(3);h[0].x=c;h[0].y=b;var d=this.getCircular(),i=this.getShadows(),g=a.bk()._nc(),s=
f.a.d(360/r),x=0,v=0,t=0;i&&0!=g.a&&!e?(x=f.u.z(g.r,s+1),v=f.u.z(g.g,s+1),t=f.u.z(g.b,s+1)):(i=!1,x=v=t=0);var B=(new f.ar)._0ar(g),w=a.j.L.c(f.m.b),D=a.h.L.c(f.m.b);d&&(i||a.c.idJ(B,l.x,l.y,2*k,2*m),a.c.idh(w,l.x,l.y,2*k,2*m));var u=j.c.a,y=u.o,E=null;y&&(E=a.a5(u,4));var z=j.getLabelsCore(),J=z.Se(),A=j.getFont(),e=e?f.m.c():j.getTextColor()._nc(),o=0!=(a.t&2)?0:270,p=g._nc(),F=B;a.c.sida(null);var C,q;q=new f._p2(0,0);n.bU.d(q,o,k,m,c,b);C=q.a;q=q.b;var L=null,K=0!=(a.r&8),M=null;K&&(L=(new n.c0)._01c0("PlotArea"),
M=j.buildGridUIObject(u));for(u=0;u<s;u++){var N=j.getVisible();N&&(K&&a.c.sida(j),G.n(a,s,z,J,A,e,C,q,o,u));o+=r;o=new f._p1(o);n.bU.e(o);var o=o.a,I=0,H=0,H=new f._p2(I,H);n.bU.d(H,o,k,m,c,b);I=H.a;H=H.b;h[1].x=C;h[1].y=q;h[2].x=I;h[2].y=H;this.getClockwise()&&n.bR.D(h[1],h[2]);C=I;q=H;i&&(F=(new f.ar)._0ar(p));K&&a.c.sida(L);d?i&&a.c.idM(F,l,o-r,r):(a.c.idN(F,h),a.c.idq(w,C,q,I,H));i&&F._d();if((0==u||y)&&N)a.c.sida(M),a.c.idq(0==u?D:E,c,b,C,q);i&&(u>=f.u.z(s+1,2)?u==s?p._cf(g):p._cf(n.bR.d(g.a,
p.r+x,p.g+v,p.b+t)):p._cf(n.bR.d(g.a,p.r-x,p.g-v,p.b-t)))}w._d();D._d();null!=E&&E._d();B._d();return s},c:function(a,c,b,k,m,l,r,j,e,h,d,i){var g=a=0;h||(c.aQ(r,e,!0),this.getFillArea()&&(h=f.e._ca(3),h[0].x=b,h[0].y=k,h[1].x=m,h[1].y=l,h[2].x=d,h[2].y=i,d=null,0!=(c.r&8)&&(d=c.c.ida(),null==this.d&&(i=(new n.bT)._0bT(c.bz,c.d,c.e,!0,!1,!1),i.g=!0,this.d=i),c.c.sida(this.d)),c.c.idN(c.x,h),0!=(c.r&8)&&c.c.sida(d)),this.getShowLines()&&c.c.idq(c.n,b,k,m,l));a=j;0>c.b.u?(g=-c.b.u,a+=g-1):g=f.a.n(c.b.u,
1);0!=c.b.d&&0==a%g&&(0!=(c.t&192)&&c.ao(c.d,e,!1)&&c.N(!0),c.ax(r,e),c.a4(b,k,0));c.c.sida(null);c.b.a.b&&-1!=e&&c.aW((new f.e)._01e(b,k),e!=j,null);return!0},l:function(){null!=this.e&&this.e.iaf(16777248)},k:function(){this.a=41},b:function(a,c){this.a=c?this.a|a:this.a&~a;this.l()},j:function(a){a=a.a;a.e=8;0==a.b&&(a.b=4096);return null}};G.n=function(a,c,b,k,m,l,r,j,e,h){var d=0,i=0;270==e||90==e?d=1:90<=e&&270>=e&&(d=2);i=180<=e?2:0;e=null;e=0!=k?b.id_(h):0!=(a.t&2)?a.j.n.a(360*h/c)+"\u00b0":
a.j.n.a(a.j.ar()?h+1:h*a.j.aS());e=a.a3(a.j,e);c=(new n.cE)._01cE(l,d,i);c.l(a.c,m,e,(new f.e)._01e(r,j));c._d()};G.m=function(a,c,b,k,m,l,r,j){var e=a.I,h=a.F;if(e>=h)return!1;var d=a.h,i=d.j;if(0==(i&64)&&(d.ar()&&d.calculateStep(a.a,a.c,m,!0),m=d.aS(),i=0==(i&1),0<m)){var g=d.n,s=d.getFont(),x=a.w(512),v=d.getLabelsCore(),t=v.Se(),B=x?f.m.c():d.getTextColor()._nc(),w=d.c.a,x=w.o,D=a.a5(w,4),B=(new n.cE)._01cE(B,0,0),k=k/(h-e),u=a.c.idV(g.a(a.F),s).c().h,y=0!=(a.r&8),E=null;y&&(E=d.buildGridUIObject(w));
for(var z=0,G=0,w=e+m;w<=h;w+=m){var A=270,o=0,p=0,p=o=(w-e)*k;x&&j&&(y&&a.c.sida(E),a.c.idh(D,c-p,b-p,2*p,2*p));for(var F=0;F<=r;F++){var C=0,q=0,q=new f._p2(C,q);n.bU.d(q,A,o,p,c,b);C=q.a;q=q.b;if(0!=F)if(x&&!j)y&&a.c.sida(E),a.c.idq(D,z,G,C,q);else break;else i&&(z=null,0!=t?(z=n.bU.a((w-e)/d.T+0.1),z=v.id_(z)):z=g.a(w),z=a.a3(d,z),y&&a.c.sida(d),B.l(a.c,s,z,(new f.e)._01e(C+4,q-f.u.z(u,2)))),a.c.idq(D,C-2,q,C+2,q);A+=l;z=C;G=q}}B._d();D._d()}return!0};G._dt("CWGR",f.Sy,0,n.icT,n.ieF)})();
